<#
Captures every web state of the prototype through the Chrome DevTools Protocol (no extra dependency:
Windows PowerShell 5.1 + an installed Chrome). Real rendering, no editing of the images.

Usage (dev server already running on http://localhost:8080):
  powershell -File tools/capture-web.ps1 -OutDir ../../docs/mockups/01-skeleton

Why CDP and not `chrome --headless --screenshot`: headless Chrome enforces a minimum window width
(about 500 px), so a 360 px viewport cannot be obtained with --window-size.
#>
param(
    [string]$BaseUrl = "http://localhost:8080/",
    [string]$OutDir = "captures-tmp",
    [string]$Chrome = "C:\Program Files\Google\Chrome\Application\chrome.exe",
    [int]$Port = 9333,
    [int]$SettleMs = 4000
)

$ErrorActionPreference = "Stop"
New-Item -ItemType Directory -Force $OutDir | Out-Null
$profileDir = Join-Path ([IO.Path]::GetTempPath()) ("proto-chrome-" + [guid]::NewGuid())

$proc = Start-Process -FilePath $Chrome -PassThru -ArgumentList @(
    "--headless=new", "--disable-gpu", "--hide-scrollbars", "--no-first-run",
    "--remote-debugging-port=$Port", "--user-data-dir=$profileDir", "about:blank")

# screen, state, file prefix
$matrix = @(
    @("empty-groups", "empty", "empty-groups-empty"),
    @("empty-groups", "empty-disabled-create", "empty-groups-disabled-create-variant"),
    @("empty-groups", "loading", "empty-groups-loading"),
    @("empty-groups", "server-error", "empty-groups-server-error"),
    @("sign-in", "empty", "dummy-sign-in-empty"),
    @("sign-in", "filled", "dummy-sign-in-filled"),
    @("sign-in", "field-error", "dummy-sign-in-field-error"),
    @("sign-in", "focus-email", "dummy-sign-in-focus-email"),
    @("sign-in", "focus-submit", "dummy-sign-in-focus-submit"),
    @("sign-in", "submitted", "dummy-sign-in-submitted")
)
$sizes = @(@("small", 360, 640), @("large", 1280, 800))
$themes = @("light", "dark")

function Invoke-Cdp($ws, [ref]$id, $method, $params) {
    $id.Value++
    $msg = @{ id = $id.Value; method = $method; params = $params } | ConvertTo-Json -Depth 10 -Compress
    $bytes = [Text.Encoding]::UTF8.GetBytes($msg)
    $null = $ws.SendAsync([ArraySegment[byte]]$bytes, 'Text', $true, [Threading.CancellationToken]::None).Result
    while ($true) {
        $buf = New-Object byte[] 4194304
        $sb = New-Object Text.StringBuilder
        do {
            $r = $ws.ReceiveAsync([ArraySegment[byte]]$buf, [Threading.CancellationToken]::None).Result
            $null = $sb.Append([Text.Encoding]::UTF8.GetString($buf, 0, $r.Count))
        } while (-not $r.EndOfMessage)
        $obj = $sb.ToString() | ConvertFrom-Json
        if ($obj.id -eq $id.Value) { return $obj }
    }
}

try {
    for ($i = 0; $i -lt 30; $i++) {
        try { $null = Invoke-RestMethod "http://localhost:$Port/json/version"; break } catch { Start-Sleep -Milliseconds 500 }
    }
    foreach ($row in $matrix) {
        foreach ($theme in $themes) {
            foreach ($size in $sizes) {
                $target = Invoke-RestMethod -Method Put "http://localhost:$Port/json/new?about:blank"
                $ws = New-Object Net.WebSockets.ClientWebSocket
                $null = $ws.ConnectAsync([Uri]$target.webSocketDebuggerUrl, [Threading.CancellationToken]::None).Result
                $id = 0
                $null = Invoke-Cdp $ws ([ref]$id) "Emulation.setDeviceMetricsOverride" @{ width = $size[1]; height = $size[2]; deviceScaleFactor = 1; mobile = $false }
                $null = Invoke-Cdp $ws ([ref]$id) "Emulation.setFocusEmulationEnabled" @{ enabled = $true }
                $null = Invoke-Cdp $ws ([ref]$id) "Emulation.setEmulatedMedia" @{ features = @(@{ name = "prefers-color-scheme"; value = $theme }) }
                $url = "${BaseUrl}?screen=$($row[0])&state=$($row[1])&theme=$theme"
                $null = Invoke-Cdp $ws ([ref]$id) "Page.navigate" @{ url = $url }
                Start-Sleep -Milliseconds $SettleMs
                $shot = Invoke-Cdp $ws ([ref]$id) "Page.captureScreenshot" @{ format = "png" }
                $file = Join-Path $OutDir ("{0}-{1}-{2}-web.png" -f $row[2], $theme, $size[0])
                [IO.File]::WriteAllBytes($file, [Convert]::FromBase64String($shot.result.data))
                $null = $ws.CloseAsync('NormalClosure', '', [Threading.CancellationToken]::None).Result
                $null = Invoke-RestMethod "http://localhost:$Port/json/close/$($target.id)"
                Write-Host "captured $file"
            }
        }
    }
} finally {
    if ($proc -and -not $proc.HasExited) { Stop-Process -Id $proc.Id -Force }
    Remove-Item -Recurse -Force $profileDir -ErrorAction SilentlyContinue
}
