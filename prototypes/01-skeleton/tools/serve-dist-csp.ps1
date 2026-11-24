<#
Serves the production web distribution with a configurable Content-Security-Policy header, to check
which CSP directives the Compose Wasm app really needs.
  ./gradlew :screens:wasmJsBrowserDistribution
  powershell -File tools/serve-dist-csp.ps1 -Csp "default-src 'self'; script-src 'self' 'wasm-unsafe-eval'" -Port 8090
#>
param(
    [string]$Root = "screens/build/dist/wasmJs/productionExecutable",
    [string]$Csp = "default-src 'self'; script-src 'self' 'wasm-unsafe-eval'; style-src 'self' 'unsafe-inline'; frame-ancestors 'none'; base-uri 'none'; form-action 'none'",
    [int]$Port = 8090
)
$rootFull = (Resolve-Path $Root).Path
$types = @{ ".html" = "text/html; charset=utf-8"; ".js" = "text/javascript"; ".wasm" = "application/wasm"; ".map" = "application/json"; ".txt" = "text/plain" }
$l = New-Object Net.HttpListener
$l.Prefixes.Add("http://localhost:$Port/")
$l.Start()
Write-Host "serving $rootFull on $Port with CSP: $Csp"
while ($l.IsListening) {
    $c = $l.GetContext()
    $p = $c.Request.Url.AbsolutePath.TrimStart("/")
    if ($p -eq "") { $p = "index.html" }
    $f = Join-Path $rootFull $p
    $c.Response.Headers.Add("Content-Security-Policy", $Csp)
    if ((Test-Path $f -PathType Leaf) -and ($f.StartsWith($rootFull))) {
        $ext = [IO.Path]::GetExtension($f)
        $c.Response.ContentType = $(if ($types.ContainsKey($ext)) { $types[$ext] } else { "application/octet-stream" })
        $b = [IO.File]::ReadAllBytes($f)
        $c.Response.OutputStream.Write($b, 0, $b.Length)
    } else { $c.Response.StatusCode = 404 }
    $c.Response.Close()
}
