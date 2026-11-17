# Module composeApp — UI Compose Multiplatform + client API

- Rôle : écrans, navigation, design system, view-models, client Ktor, point
  d'entrée web (wasmJsMain).
- Cibles : android, wasmJs (+ js en repli).
- Design system : src/commonMain/kotlin/com/expensesshared/designsystem/ ; réutiliser
  avant de créer ; aucune couleur ou taille en dur dans les écrans.
- Accessibilité (RGAA / WCAG AA) : contentDescription / semantics sur tout
  élément interactif, cibles ≥ 48 dp, contrastes AA clair et sombre, ordre de
  focus et navigation clavier sur le web, champs e-mail / mot de passe déclarés
  comme tels (autofill).
- Sécurité web : aucun jeton en localStorage ; session par cookie HttpOnly /
  Secure / SameSite posé par le serveur ; CSP stricte dans index.html.
- Sécurité Android : jetons via Android Keystore, jamais en clair.
- Messages d'erreur génériques (ne révèlent pas l'existence d'un compte).
- prototypes/ : maquettes uniquement, exclu du build de production.
- Tests : commonTest (logique UI, runComposeUiTest pour les états d'écran).
- Commandes : ./gradlew :composeApp:allTests  ·  ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
