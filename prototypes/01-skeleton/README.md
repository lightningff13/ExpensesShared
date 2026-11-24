# Prototype 01 — Squelette (US-1 et US-2)

Build Gradle **autonome** (décision senior Q17), non inclus dans un build racine, code de
prototype uniquement, jamais en production. Données fictives, aucun appel réseau, aucun
stockage, aucune journalisation des valeurs saisies. Package `com.expensesshared.prototypes.skeleton`.

Les libellés sont des **PROPOSITIONS** en attente des réponses du senior (Q7 : écran vide,
Q10 : formulaire factice, Q8 : états chargement / erreur, conditionnels).

## Lancer les maquettes

Prérequis : JDK 21 (celui d'Android Studio convient), Android SDK (platform 36), variable
`ANDROID_HOME` (Windows PowerShell : `$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"`, à refaire
dans chaque nouveau terminal, ou à définir pour le compte avec
`[Environment]::SetEnvironmentVariable("ANDROID_HOME", "$env:LOCALAPPDATA\Android\Sdk", "User")`).
Toujours lancer **depuis `prototypes/01-skeleton/`** : le dépôt n'a pas encore de build racine. Sous
Windows PowerShell, remplacer `./gradlew` par `.\gradlew.bat`.

| Usage | Commande |
|---|---|
| Previews | ouvrir `screens/src/commonMain/.../gallery/Previews.kt` dans Android Studio (volet Preview) ; 2 tailles (360 dp, 1280 dp) × clair / sombre par état |
| Installer sur Android | `./gradlew :androidApp:installDebug` puis `adb shell am start -n com.expensesshared.prototypes.skeleton/com.expensesshared.prototypes.skeleton.android.MainActivity --es screen sign-in --es state filled --es theme dark` |
| Navigateur (Wasm) | `./gradlew :screens:wasmJsBrowserDevelopmentRun` puis `http://localhost:8080/?screen=sign-in&state=filled&theme=dark` |
| Distribution web | `./gradlew :screens:wasmJsBrowserDistribution` |
| Captures web | `powershell -File tools/capture-web.ps1 -OutDir ../../docs/mockups/01-skeleton` (serveur de dev lancé) |
| Test de CSP | `powershell -File tools/serve-dist-csp.ps1 -Port 8092 -Root screens/build/dist/wasmJs/productionExecutable` |

Paramètres (`screen`, `state`, `theme` ; extras d'intent sur Android, requête d'URL sur le web) :
- `screen=empty-groups` : `empty`, `empty-disabled-create` (variante Q7), `loading`, `server-error` (conditionnels Q8).
- `screen=sign-in` : `empty`, `filled`, `field-error`, `focus-email`, `focus-submit`, `submitted`.
- `theme=light|dark|system`. Aucun chrome ajouté : l'ordre de focus et la sémantique sont ceux de l'écran.

## Dépendances (nom, version, registre, raison)

Toutes vérifiées par lecture des `maven-metadata.xml` et des dates de publication des `.pom`
(version stable publiée il y a plus de 14 jours le 2026-11-22). Versions fixées dans
`gradle/libs.versions.toml`.

| Élément | Version | Registre | Pourquoi |
|---|---|---|---|
| Gradle (wrapper, `distributionSha256Sum` = 21bd5da2…7607, identique au `.sha256` officiel) | 9.8.2 (2026-10-19) | services.gradle.org | build ; 9.9.x trop récent (< 14 j) |
| `org.jetbrains.kotlin.multiplatform` + `plugin.compose` | 2.4.21 (2026-10-08) | Gradle Plugin Portal / Maven Central | Kotlin Multiplatform et compilateur Compose |
| `org.jetbrains.compose` (plugin) | 1.11.1 (2026-06-01) | Gradle Plugin Portal / Maven Central | Compose Multiplatform. 1.12.x écartée : elle tire androidx.compose 1.12 qui exige compileSdk 37, platform non installée |
| `com.android.application` et `com.android.kotlin.multiplatform.library` | 9.5.1 (2026-11-05) | Google Maven | AGP 9 : un module KMP Android est une bibliothèque `kotlin.multiplatform.library`, l'application est un module séparé. 9.5.2 écartée (2026-11-20, trop récente) |
| `androidx.activity:activity-compose` | 1.13.0 | Google Maven | `ComponentActivity.setContent`, edge-to-edge |
| `org.jetbrains.compose.runtime:runtime`, `foundation`, `ui`, `components-ui-tooling-preview` | 1.11.1 | Maven Central | UI et `@Preview` commun |
| `org.jetbrains.compose.material3:material3` | 1.9.0 | Maven Central | Material 3 (défauts) |
| `compose.uiTooling` (androidRuntimeClasspath) | aligné sur le plugin | Google Maven | rendu des previews dans Android Studio |

**À signaler (chaîne d'approvisionnement)** : la cible wasmJs fait télécharger au plugin Kotlin, au
moment du build, **Node.js 26.2.0** (nodejs.org/dist), **Yarn** (GitHub releases) et des paquets **npm**
(webpack 5.108.1 observé dans le journal, webpack-cli, dev-server, etc.) avec un `kotlin-js-store/wasm/yarn.lock`
généré. Ces sources ne sont pas dans la liste « Maven Central / Google Maven / npm » validée une à une :
à examiner par le senior avant F1 (verrouillage du lockfile, `npm audit`). Le build web n'a pas fonctionné avec
`FAIL_ON_PROJECT_REPOS` (le plugin ajoute ses dépôts au projet racine) : mode par défaut conservé.
Le `gradle-wrapper.jar` a été généré par la tâche officielle `wrapper` de Gradle 9.3.1 (distribution locale) pour la
distribution 9.8.2 ; à régénérer par le senior avec `./gradlew wrapper` si souhaité.

## Résultats d'accessibilité (preuves : captures + inspection du navigateur)

### Contrastes WCAG AA (calculés sur les jetons exacts de `PrototypeTheme.kt`)

| Thème | Paire | Ratio | Seuil |
|---|---|---|---|
| Clair | titre / fond | 16,71 | 4,5 OK |
| Clair | texte secondaire / fond | 9,11 | 4,5 OK |
| Clair | bandeau (texte / conteneur d'erreur) | 12,77 | 4,5 OK |
| Clair | libellé du bouton (blanc / primaire) | 6,44 | 4,5 OK |
| Clair | message d'erreur / fond | 6,38 | 4,5 OK |
| Clair | bordure des champs / fond | 4,44 | 3 OK |
| Clair | indicateur de focus des champs (primaire) / fond | 6,28 | 3 OK |
| Clair | bordure d'erreur ; indicateur de chargement | 6,38 ; 6,28 | 3 OK |
| Sombre | titre / fond | 13,27 | 4,5 OK |
| Sombre | texte secondaire / fond | 10,05 | 4,5 OK |
| Sombre | bandeau | 7,17 | 4,5 OK |
| Sombre | libellé du bouton | 7,71 | 4,5 OK |
| Sombre | message d'erreur / fond | 10,03 | 4,5 OK |
| Sombre | bordure des champs / fond | 5,41 | 3 OK |
| Sombre | focus des champs ; bordure d'erreur ; chargement | 10,05 ; 10,03 ; 10,05 | 3 OK |

Le bouton désactivé de la variante Q7 est exempté par WCAG (composant inactif).

**Non conforme (constaté)** : l'indicateur de focus du **bouton** (superposition d'état Material, quelques pour cent
de variation) est quasi invisible (capture `dummy-sign-in-focus-submit-*`). Le critère 3:1 (CA-2.1) n'est pas tenu
pour le bouton : il faut un indicateur de focus dédié (anneau) dans le design system.

### Cibles ≥ 48 dp
Champs (hauteur M3 56 dp, `heightIn(min = 48.dp)`), boutons (`heightIn(min = 48.dp)`, largeur pleine sur le
formulaire). Mesuré dans l'arbre web : champs 65 px, bouton 44 px **CSS pixels dans la fenêtre d'inspection de
532 px** — le bouton mesure **44 px** (hauteur visuelle M3 40 dp + marge de zone tactile) : écart à vérifier,
la zone cliquable M3 est étendue à 48 dp (`minimumInteractiveComponentSize`) mais l'élément a11y exposé par le
web ne fait que 44 px. Le bouton « Réessayer » et le bouton désactivé suivent la même règle. À confirmer sur appareil.

### Lecteur d'écran web (inspecté dans le DOM, aucun NVDA / VoiceOver disponible ici)
Compose Wasm 1.11 construit un arbre d'accessibilité dans un shadow DOM (`#cmp_a11y_root`, `aria-live=polite`),
le canevas étant `role=generic`. Observé :
- titre : `role=heading` OK ; bandeau : texte exposé OK ; bouton : `role=button`, nom « Se connecter » OK.
- champs : `div contenteditable role=textbox`, **sans nom accessible** (le libellé « Adresse e-mail » / « Mot de passe »
  n'est pas exposé : ni `aria-label`, ni `aria-labelledby`), **sans `type=email` / `type=password`, sans `autocomplete`**,
  sans `aria-invalid`. Le champ mot de passe est un simple `textbox` (le masquage n'est que visuel).
  => CA-2.2 et CA-2.3 non tenus sur le web avec cette configuration ; c'est le point critique de l'étude ADR-0001.
- Gestionnaire de mots de passe (CA-2.4) : aucun champ `<input>` natif n'existe, donc rien à détecter
  par Chrome / Firefox ; non testé avec un vrai gestionnaire (aucun identifiant réel saisi).
- Messages d'erreur : région `aria-live=polite` globale ; l'annonce du message d'erreur du champ n'a pas été
  vérifiée avec un vrai lecteur d'écran.

### Clavier web (navigateur intégré, touches réelles)
- Tab entre sur le canevas, puis ordre e-mail → mot de passe → bouton, puis sortie de la page, puis retour au
  champ e-mail : l'ordre est conforme. Indicateur visible sur les champs (bordure primaire + libellé flottant),
  quasi invisible sur le bouton (voir ci-dessus). L'observation en capture d'écran présente un léger décalage
  de rendu d'un cycle ; l'ordre a été confirmé sur plusieurs cycles mais pas exhaustivement (Maj+Tab, Entrée,
  Espace non testés).

### Clavier web, compléments (Shift+Tab, Entrée, Espace)
- Maj+Tab : du bouton vers le mot de passe, conforme.
- Entrée dans le champ mot de passe : aucun message « factice » affiché (l'action IME Terminé n'est pas
  déclenchée par la touche Entrée matérielle observée ici).
- Entrée et Espace sur le bouton : aucun message « factice » obtenu, mais les événements clavier étaient synthétiques
  (outil du navigateur intégré) et le focus réel sur le bouton n'a pas pu être confirmé : **non concluant**, à refaire
  à la main.
- L'arbre d'accessibilité expose la valeur saisie : l'e-mail en clair et le mot de passe sous forme de 20 puces
  (la longueur du mot de passe est donc lisible par un lecteur d'écran / un script).

### Android (appareil physique Pixel 7 Pro, Android SDK 37, autorisé par le senior le 2026-11-24)
Réalisé par la session principale. Valeurs d'origine relevées avant toute modification, puis restaurées et
vérifiées : mode nuit oui, taille et densité physiques (1080x2340, 420), font_scale 1.0, aucun service
d'accessibilité, accessibility_enabled 0 ; prototype désinstallé.
- **Arbre d'accessibilité** (`uiautomator dump`) : champ e-mail = `EditText` avec la valeur, champ mot de passe =
  `EditText` **`password=true`** (contrairement au web), libellés « Adresse e-mail » / « Mot de passe » présents
  comme textes enfants, message d'erreur et message « factice » présents, bouton = élément cliquable et
  focusable contenant « Se connecter ». Le dump n'expose ni le rôle titre, ni l'état d'erreur, ni la fusion des
  nœuds faite par TalkBack : il ne remplace pas l'écoute.
- **Remplissage automatique Android** : en focalisant le champ e-mail, le clavier propose aussitôt le compte
  réel de l'appareil (bandeau de suggestions) et l'icône du gestionnaire de mots de passe : le champ est bien
  détecté comme e-mail (parité CA-2.4 côté Android). Capture supprimée car elle montrait un compte réel ; les
  captures `focus-email` sont prises clavier masqué.
- **TalkBack** : activé, mais son tutoriel de bienvenue s'ouvre au premier lancement et les balayages
  injectés par adb ne le pilotent pas. **Écoute non réalisée** (annonces et ordre de parcours) : à faire à la
  main par le senior. TalkBack désactivé et réglages restaurés.
- **Encart système** : le bandeau « formulaire factice » est dessiné sous la barre d'état (edge-to-edge sans
  `windowInsetsPadding`) : défaut de prototype à corriger dans `ScreenContainer`.
- **Focus du bouton** : en mode tactile, aucun indicateur visible (attendu sur Android) ; le défaut d'anneau
  de focus reste un sujet web / clavier.

### Messages d'erreur et actions irréversibles
- Erreur de champ : « Saisissez une adresse e-mail. » (seulement « champ vide » ; aucune règle de format inventée ;
  ne dit rien sur l'existence d'un compte). Erreur serveur : « Une erreur est survenue… Réessayez plus tard. »
  (sans code, sans nom d'hôte, sans détail technique).
- Aucune action irréversible dans ces écrans (pas de suppression ni d'envoi) : confirmation sans objet en F1.
  La soumission du formulaire factice n'émet aucune requête ni écriture (non observé sur Android).

## Contraintes et divergences de plateforme

1. **CSP** : le build Wasm exige `'wasm-unsafe-eval'` dans `script-src`. Mesuré : avec
   `default-src 'self'; script-src 'self'; style-src 'self' 'unsafe-inline'` l'application ne démarre pas
   (`CompileError ... violates ... 'unsafe-eval'`) ; avec `default-src 'self'; script-src 'self' 'wasm-unsafe-eval';
   style-src 'self' 'unsafe-inline'; frame-ancestors 'none'; base-uri 'none'; form-action 'none'` elle s'affiche.
   La nécessité de `style-src 'unsafe-inline'` n'a pas été isolée (non testé sans). `frame-ancestors` et `form-action`
   ne s'appliquent pas via `<meta>` : à poser par en-tête HTTP. Aucune requête vers une origine externe (pas de CDN ni
   de police distante) observée : trois ressources de même origine seulement.
2. **Taille** (distribution de production, octets bruts / gzip) : `skiko` wasm 8 652 729 / 3 328 198 ;
   application wasm 2 803 626 / 859 652 ; JS 533 586 / 100 006 ; total ≈ 12,0 Mo brut, ≈ 4,3 Mo gzip.
   Les source maps (1,5 Mo) sont produites dans la distribution (Q1 : à ne pas publier).
3. **Premier affichage** (mesure locale, serveur de dev, navigateur intégré, cache chaud, machine de dev) :
   First Contentful Paint 712 ms, ressources chargées en 433 ms. Mesure indicative uniquement : ni réseau limité,
   ni cache vide, ni appareil cible (critère 6 / Q2 non évalué).
4. **Rendu du texte** : accents (é, ê, è, ç, —) et apostrophe correctement rendus dans Chrome (Windows) avec la
   police par défaut, aucune police embarquée. Le rendu sur Linux / macOS / Safari n'a pas été testé (Q5).
5. **Android / AGP 9** : module KMP Android = `com.android.kotlin.multiplatform.library` ; l'application doit
   être un module séparé (`:androidApp`). Les previews communes ne gèrent pas `uiMode` : le thème sombre est
   forcé par paramètre. Les états de focus ne sont pas rendus de façon fiable dans une preview statique.
6. **Sémantique Compose sur le web** : les champs ne sont pas des `<input>` natifs (voir a11y) ; l'anneau de focus du
   bouton est trop discret ; l'ordre Tab commence par le canevas.
7. **compileSdk** : Compose Multiplatform 1.12.x impose compileSdk 37 (non installé ici) : version 1.11.1 retenue.
8. Le dépôt interdit tout `@Suppress` non justifié : aucun n'est utilisé.

## Captures

Dossier `docs/mockups/01-skeleton/`, nom `<écran>-<état>-<thème>-<taille>-<plateforme>.png`
(petit = 360×640, grand = 1280×800 ; clair / sombre forcés par le paramètre `theme` et par `prefers-color-scheme`).

- **Web : 40 captures présentes** (10 états × 2 thèmes × 2 tailles), prises par Chrome 
  (protocole DevTools, `tools/capture-web.ps1`), sans retouche.
- **Android : 40 captures présentes** (10 états × 2 thèmes × 2 tailles), Pixel 7 Pro, `adb exec-out screencap`.
  Petit = densité forcée 480 (largeur 360 dp) ; grand = densité forcée 200 (largeur 864 dp) ; thème par
  `cmd uimode night` + paramètre `theme`. Retouche limitée à la confidentialité : barre d'état et barre de
  navigation retirées (petit) ou icônes de la barre d'état masquées par la couleur de fond (grand), barre des
  tâches retirée. L'émulateur reste inutilisable (accélération matérielle absente).

## Composants manquants du design system (non créés)

1. Jetons : palette claire / sombre, échelle d'espacements (`screenPadding`, `stackGap`, largeur max de contenu),
   typographie (et police embarquée si besoin), taille minimale de cible (48 dp) — ici dans `theme/PrototypeTheme.kt`.
2. `AppTheme` (clair / sombre).
3. `ScreenContainer` (défilement, marges, largeur maximale centrée).
4. `EmptyState` (titre `heading`, texte, action optionnelle).
5. `LoadingIndicator` (avec description accessible et région live).
6. `ErrorState` / message d'erreur générique avec action « Réessayer ».
7. `AppTextField` (libellé, erreur, texte d'aide, annonce de l'erreur) et variantes `EmailField` / `PasswordField`
   (clavier, sémantique de remplissage automatique, masquage, éventuel bouton afficher / masquer).
8. `PrimaryButton` (cible ≥ 48 dp, **anneau de focus visible ≥ 3:1**, état désactivé).
9. `Banner` / `Alert` (avertissement « formulaire factice »).
10. Style d'indicateur de focus commun (clavier web).

## Fichiers créés

Dans `prototypes/01-skeleton/` : `settings.gradle.kts`, `build.gradle.kts`, `gradle.properties`,
`gradle/libs.versions.toml`, `gradle/wrapper/*`, `gradlew`, `gradlew.bat`, `.gitignore`, `README.md`,
`kotlin-js-store/` (verrou npm généré), `tools/capture-web.ps1`, `tools/serve-dist-csp.ps1`,
`screens/build.gradle.kts`, `screens/src/commonMain/kotlin/com/expensesshared/prototypes/skeleton/`
(`theme/PrototypeTheme.kt`, `emptygroups/EmptyGroupsScreen.kt`, `signin/DummySignInForm.kt`,
`gallery/PrototypeGallery.kt`, `gallery/Previews.kt`), `screens/src/wasmJsMain/kotlin/.../Main.kt`,
`screens/src/wasmJsMain/resources/index.html`, `androidApp/build.gradle.kts`,
`androidApp/src/main/AndroidManifest.xml` (aucune permission, `allowBackup=false`),
`androidApp/src/main/res/values/themes.xml`, `androidApp/src/main/kotlin/.../android/MainActivity.kt`.
Dans `docs/mockups/01-skeleton/` : 40 captures web.
