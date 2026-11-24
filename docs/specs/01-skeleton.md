# Spec F1 — Squelette et étude d'accessibilité Wasm
_Branche feat/skeleton — phase 1 terminée, **GATE 1 approuvée le 2026-11-24** ; prochaine phase : 2 (plan technique)_

## Contexte et objectif
Poser le squelette exécutable du projet sur les trois cibles (Android, web Wasm,
serveur Ktor) avec une CI verte et une build de test par canal, sans aucune
règle métier. Mener l'étude d'accessibilité exigée par l'ADR-0001 pour
confirmer (go) ou infirmer (no-go) l'interface web en Compose Multiplatform Wasm
avant F2.

## Utilisateurs et rôles
- Visiteur : ouvre l'application (pas de compte en F1).
- Senior / opérateur : installe les builds de test, consulte la santé du serveur,
  mène l'étude d'accessibilité.

## Périmètre
- Inclus : modules Gradle vides mais câblés (shared:domain, shared:contract,
  composeApp, androidApp, server), écran « aucun groupe », formulaire e-mail /
  mot de passe factice (étude), endpoint de santé, CI, APK installée par adb,
  web et serveur lancés en local (localhost), étude ADR-0001.
- Exclu : comptes, authentification réelle, groupes, dépenses, montants,
  persistance applicative, i18n, iOS, extension ; **aucun serveur distant ni
  publication sur Google Play** (décision du senior, 2026-11-24 : application de test).

## Plateformes et parité
| Story | Android | Web | Serveur | Parité |
|---|---|---|---|---|
| US-1 Écran « aucun groupe » | oui | oui | — | identique |
| US-2 Formulaire factice (étude) | oui, build de test (Q10-bis) | oui, build de test | — | identique |
| US-3 Santé du serveur | — | — | oui | — |
| US-4 Exécution locale par cible | APK installée par adb | distribution servie sur localhost | serveur sur localhost | adaptée (mode de lancement propre à chaque cible) |
| US-5 CI verte | oui | oui | oui | identique |
| US-6 Étude a11y ADR-0001 | TalkBack (contrôle de parité) | critères 1 à 6 | — | adaptée |

## 1a. User stories

### US-1 — Écran « aucun groupe »
En tant que visiteur, je veux voir un écran clair indiquant que je n'ai aucun
groupe afin de comprendre que l'application fonctionne et qu'elle est vide.
Plateformes : Android, web — parité identique.

- CA-1.1 — Given l'application installée (Android) ou l'URL de test ouverte (web),
  When je la lance, Then l'écran « aucun groupe » s'affiche avec un titre et un
  texte d'état vide (libellés : Q7).
- CA-1.2 — Given l'écran affiché, When TalkBack (Android) ou le lecteur d'écran Windows
  (web) le parcourt, Then le titre est annoncé comme en-tête et le texte est lu
  dans l'ordre visuel.
- CA-1.3 — Given le thème système clair ou sombre, When l'écran s'affiche,
  Then les contrastes respectent WCAG AA (4,5:1 texte, 3:1 éléments graphiques).
- CA-1.4 — Given un petit écran (360 dp) ou un grand écran (≥ 840 dp / 1280 px),
  When l'écran s'affiche, Then aucun contenu n'est tronqué ni chevauché.
- CA-1.5 — Given le zoom navigateur à 200 % ou la taille de police système à
  200 % (Android), When l'écran s'affiche, Then aucun contenu ni fonction n'est perdu.
- CA-1.6 — Given aucune donnée réelle, When l'écran s'affiche, Then il n'affiche
  ni ne stocke aucune donnée personnelle.
- États : « vide » seulement. L'application est hors ligne en F1 (Q8) : pas
  d'état « chargement » ni « erreur serveur » (maquettes conservées pour F2,
  hors périmètre F1). « Accès refusé », « session expirée », « succès » : sans
  objet en F1 (pas de compte).

### US-2 — Formulaire e-mail / mot de passe factice (étude ADR-0001)
En tant que senior, je veux un formulaire e-mail / mot de passe factice afin de
vérifier l'accessibilité et le remplissage par gestionnaire de mots de passe
avant de bâtir l'authentification (F4).
Plateformes : web (obligatoire), Android (Q10).

- CA-2.1 — Given le formulaire affiché, When je le parcours au clavier
  (Tab / Maj+Tab), Then le focus passe e-mail → mot de passe → bouton dans cet
  ordre, avec un indicateur de focus visible (contraste ≥ 3:1).
- CA-2.2 — Given le focus sur un champ, When un lecteur d'écran l'annonce,
  Then il annonce son rôle (champ de saisie / champ de mot de passe), son
  libellé et son état (vide, rempli, erreur).
- CA-2.3 — Given le champ e-mail, Then il est déclaré comme e-mail (type de
  clavier, sémantique de remplissage automatique) ; le champ mot de passe est
  déclaré comme mot de passe et masqué par défaut.
- CA-2.4 — Given Chrome ou Firefox avec leur gestionnaire de mots de passe
  intégré, When je focalise les champs, Then le gestionnaire propose le
  remplissage (identifiants fictifs uniquement).
- CA-2.5 — Given un e-mail vide, When j'active le bouton (Entrée ou Espace),
  Then un message d'erreur générique est affiché sous le champ et annoncé par
  le lecteur d'écran (validation de format : Q10, pas de règle métier inventée).
- CA-2.6 — Given des valeurs saisies, When j'active le bouton, Then aucune
  requête réseau n'est émise, rien n'est stocké ni journalisé, et un message
  indique que le formulaire est factice (comportement exact : Q10).
- CA-2.7 — Given une build de production, Then le formulaire n'existe pas
  (présent uniquement dans les builds de test, Q10).

### US-3 — Santé du serveur
En tant qu'opérateur, je veux un endpoint de santé afin de vérifier que le
backend déployé répond.
Plateforme : serveur.

- CA-3.1 — Given le serveur démarré, When j'appelle GET /health, Then il
  répond 200 avec un corps limité au statut (Q9) : ni version, ni
  vérification de la base, ni information sur l'hôte.
- CA-3.2 — Given une méthode autre que GET, When j'appelle /health, Then il
  répond 405 sans détail technique.
- CA-3.3 — Given une route inconnue, When je l'appelle, Then il répond 404 au
  format générique, sans trace de pile ni version de framework.
- CA-3.4 — Given une réponse quelconque, Then les en-têtes ne révèlent ni la
  version de Ktor ni celle de la JVM.
- CA-3.5 — Given une variable d'environnement obligatoire absente, When le
  serveur démarre, Then il refuse de démarrer avec un message qui nomme la
  variable sans afficher de valeur (aucune valeur par défaut pour un secret).

### US-4 — Exécution locale sur chaque cible
En tant que senior, je veux lancer le squelette en local sur chaque cible afin
de le valider sur appareil réel et navigateur, sans hébergement ni store.

- CA-4.1 (Android) — Given la branche fusionnée, When je lance
  `assembleDebug` puis `adb install` de l'APK produite, Then l'application
  s'installe sur un appareil Android 10+ (minSdk 29) et affiche l'écran
  « aucun groupe » ; une commande documentée la désinstalle. Signature : clé de
  débogage locale générée par l'outillage Android, jamais commitée (Q11-bis).
- CA-4.2 (Android) — Given la build release construite en CI (non signée,
  non installée), Then elle est non débogable, R8 activé, et son manifeste
  fusionné ne déclare aucune permission.
- CA-4.3 (web) — Given la distribution Wasm de production, When je la sers en
  local par la commande documentée, Then elle s'affiche sur
  `http://localhost:PORT` avec la CSP de la spec (balise meta dans index.html ;
  `frame-ancestors` non applicable par meta, voir STRIDE web).
- CA-4.4 (serveur) — Given `:server:run`, Then le serveur écoute uniquement sur
  127.0.0.1 (jamais 0.0.0.0) et GET `http://localhost:PORT/health` répond 200.

### US-5 — CI verte
En tant que senior, je veux une CI bloquante sur chaque PR afin qu'aucun
changement ne soit fusionné sans contrôles.

- CA-5.1 — Given une PR, When la CI tourne, Then lint (detekt, ktlint,
  Android Lint), tests (toutes cibles), build Android et Wasm, tests serveur,
  gitleaks, Semgrep, CodeQL et audit des dépendances passent.
- CA-5.2 — Given un changement dans shared/, Then toutes les cibles sont construites.
- CA-5.3 — Given des tests d'architecture (outil : Q3), When un import interdit
  par l'ADR-0002 est ajouté, Then la CI échoue.
- Le senior déplace lui-même docs/ci/ci-proposal.yml vers .github/workflows/.

### US-6 — Étude d'accessibilité ADR-0001
En tant que senior, je veux un rapport go / no-go mesuré sur US-1 et US-2 afin
de confirmer ou remplacer l'interface web Compose Wasm avant F2.

- CA-6.1 — Critère 1 : NVDA (Windows ; VoiceOver macOS hors cible) annonce rôle,
  libellé et état de chaque élément interactif de US-1 et US-2.
- CA-6.2 — Critère 2 : parcours complet au clavier, focus toujours visible.
- CA-6.3 — Critère 3 : zoom navigateur 200 % sans perte.
- CA-6.4 — Critère 4 : gestionnaires de Chrome et Firefox détectent les champs.
- CA-6.5 — Critère 5 : deux dernières versions de Chrome, Firefox et Edge en
  Wasm (Safari hors cible, Q14) ; le repli JS n'est construit que si Wasm échoue sur l'un
  d'eux (Q12).
- CA-6.6 — Critère 6 : First Contentful Paint ≤ 5 s, réseau simulé « Fast 4G »
  de Chrome DevTools, cache vide (Q2) ; le temps jusqu'à la première image
  Compose est relevé à titre d'information.
- CA-6.7 — Given l'étude terminée, Then un rapport (docs/adr/0001-study-report.md, Q16) liste chaque critère × navigateur avec résultat, preuve
  (capture, enregistrement) et décision go / no-go du senior ; l'ADR-0001 passe
  en « confirmée » ou un nouvel ADR est ouvert.
- Contrôle de parité Android : TalkBack et remplissage automatique Android sur
  US-1 (et US-2 si Q10).

## 1b. Maquettes (mockup-designer + session principale pour l'appareil Android)
Build Gradle autonome dans prototypes/01-skeleton/ (décision Q17), non inclus dans
le build racine ; détail des dépendances, commandes et mesures dans
prototypes/01-skeleton/README.md. Libellés = propositions (Q7, Q8, Q10).

| Écran | Stories | États | Captures (docs/mockups/01-skeleton/) |
|---|---|---|---|
| EmptyGroupsScreen | US-1 | vide ; variante bouton « Créer » désactivé (Q7) ; chargement et erreur serveur (conditionnels, Q8) | empty-groups-<état>-<thème>-<taille>-<android\|web>.png |
| DummySignInForm | US-2 | vide, rempli, erreur de champ, focus e-mail, focus bouton, soumis | dummy-sign-in-<état>-<thème>-<taille>-<android\|web>.png |

- 80 captures : 10 états × clair / sombre × petit (360 dp / 360 px) / grand
  (864 dp / 1280 px) × Android (Pixel 7 Pro physique) et web (Chrome, Wasm).
  Captures Android : barres système retirées ou masquées (confidentialité).
- Lancer **depuis prototypes/01-skeleton/** (le dépôt n'a pas encore de build
  racine), avec `ANDROID_HOME` défini (Windows :
  `$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"`) :
  - web : `.\gradlew.bat :screens:wasmJsBrowserDevelopmentRun` (Windows) ou
    `./gradlew …` (macOS / Linux), puis
    `http://localhost:8080/?screen=sign-in&state=filled&theme=dark` ;
  - Android : `.\gradlew.bat :androidApp:installDebug` (installe sur l'appareil
    connecté ; désinstaller ensuite avec
    `adb uninstall com.expensesshared.prototypes.skeleton`).

### Constats d'accessibilité (à confirmer par l'étude formelle US-6)
| Constat | Android | Web (Wasm) | Critère touché |
|---|---|---|---|
| Contrastes AA clair / sombre | OK (mêmes jetons) | OK (ratios dans le README) | CA-1.3 |
| Titre exposé comme en-tête | non visible dans le dump (écoute TalkBack à faire) | `role=heading` OK | CA-1.2 |
| Nom accessible des champs | libellé présent comme texte enfant | **absent** (textbox sans nom) | CA-2.2, critère 1 ADR |
| Type e-mail / mot de passe, autocomplete | `password=true` ; e-mail détecté par le remplissage automatique | **absents** ; pas d'`<input>` natif | CA-2.3, CA-2.4, critères 1 et 4 ADR |
| Longueur du mot de passe | masquée par des puces (standard Android) | **20 puces exposées** dans l'arbre a11y | CA-2.2 (fuite mineure) |
| Ordre de focus clavier | sans objet (tactile) | e-mail → mot de passe → bouton, Maj+Tab OK | CA-2.1, critère 2 ADR |
| Focus visible du bouton | sans objet en tactile | **quasi invisible** (< 3:1) | CA-2.1 |
| Entrée / Espace sur le bouton, Entrée dans le mot de passe | — | **non concluant** (à refaire à la main) | CA-2.5, CA-2.6 |
| Cible ≥ 48 dp | OK (mesures du dump) | bouton exposé à 44 px (à vérifier) | — |
| Bandeau sous la barre d'état | **défaut** (encart système non géré) | sans objet | CA-1.4 |
| Écoute lecteur d'écran | **non faite** (TalkBack non pilotable par adb) | **non faite** (NVDA / VoiceOver absents) | critère 1 ADR (Q14) |

Signal précoce : sur le web, les critères 1 et 4 de l'ADR-0001 échouent dans la
configuration actuelle de Compose Multiplatform 1.11.1. Ce n'est pas encore la
décision no-go : l'étude US-6 doit vérifier s'il existe une configuration ou une
version qui les satisfait, et mesurer avec le lecteur d'écran Windows.

### Contraintes de plateforme relevées
- CSP web : `'wasm-unsafe-eval'` indispensable ; `frame-ancestors` et
  `form-action` uniquement par en-tête HTTP ; `style-src 'unsafe-inline'` non isolé.
- Poids web : ≈ 4,3 Mo gzip (skiko 3,3 Mo) ; FCP indicatif 712 ms (cache chaud,
  machine de dev, ne vaut pas mesure Q2) ; source maps produites (1,5 Mo).
- AGP 9 : module KMP Android = `com.android.kotlin.multiplatform.library`,
  application dans un module séparé (impact sur le plan F1).
- Compose Multiplatform 1.12 exige compileSdk 37 : 1.11.1 retenue pour le prototype.
- La cible wasmJs télécharge Node.js, Yarn et des paquets npm au build (Q18).
- Remplissage automatique Android : le clavier propose le compte réel de
  l'appareil dès le focus du champ e-mail (consigne pour les testeurs).

### Composants manquants du design system (Q5)
Jetons (palette, espacements, typographie, cible 48 dp), AppTheme, ScreenContainer
(avec encarts système), EmptyState, LoadingIndicator, ErrorState, AppTextField +
EmailField / PasswordField, PrimaryButton avec anneau de focus ≥ 3:1, Banner,
style de focus commun.

## 1c. Analyse STRIDE

### Surface d'attaque
- Points d'entrée : GET /health (et toute route inconnue) ; distribution web
  statique (index.html, .wasm, .js) ; application Android (Activity de
  lancement) ; formulaire factice ; pipeline CI (PR, dépendances, actions) ;
  APK installée par adb ; serveurs locaux (Ktor, distribution web).
- Données sensibles : aucune donnée métier ; identifiants fictifs saisis dans le
  formulaire (risque de saisie de vrais identifiants par un testeur) ; aucun
  secret en F1 (pas de base selon Q9-bis, pas de clé de publication).
- Frontières de confiance : poste du développeur ↔ réseau local (serveurs
  locaux) ; poste ↔ appareil Android (adb) ; CI ↔ registres de dépendances.

### Menaces — shared (domain, contract)
| Catégorie | Menace concrète | Prob. | Impact | Mesure | Test associé |
|---|---|---|---|---|---|
| Spoofing | Dépendance typosquattée ajoutée au catalogue de versions | Faible | Élevé | Vérification d'existence et de maintenance de chaque dépendance ; versions fixées dans libs.versions.toml ; dependency-review en CI | Revue PR + job dependencies |
| Tampering | Wrapper Gradle altéré exécutant du code en build | Faible | Élevé | gradle/actions/wrapper-validation en CI ; somme de contrôle de la distribution dans gradle-wrapper.properties | Job lint (wrapper-validation) |
| Repudiation | — (aucune action utilisateur) | — | — | Sans objet en F1 | — |
| Information disclosure | Champ inconnu accepté silencieusement à la désérialisation | Faible | Moyen | Json configuré avec ignoreUnknownKeys = false dès F1 | Test commonTest : JSON avec champ inconnu rejeté |
| Denial of service | — | — | — | Sans objet en F1 | — |
| Elevation of privilege | Dépendance de framework introduite dans shared:domain | Moyen | Moyen | Graphe Gradle : domain sans dépendance ; test d'architecture (Q3) | Test d'architecture + CI |

### Menaces — server
| Catégorie | Menace concrète | Prob. | Impact | Mesure | Test associé |
|---|---|---|---|---|---|
| Spoofing | Machine du réseau local qui joint le serveur de développement | Faible | Faible | Écoute sur 127.0.0.1 uniquement ; HTTP en clair accepté car local (aucun serveur distant en F1) | Test de config : adresse d'écoute = 127.0.0.1 |
| Tampering | Requête inattendue (méthode, corps) sur /health | Faible | Faible | Route GET uniquement ; pas de lecture de corps | testApplication : POST /health → 405 |
| Repudiation | Absence de trace des appels | Faible | Faible | Journal d'accès minimal (méthode, chemin, statut, durée), sans corps ni en-têtes sensibles ; IP : Q9 / RGPD | Test : journal ne contient ni corps ni Authorization |
| Information disclosure | Trace de pile, version Ktor/JVM ou variables d'environnement exposées dans une réponse d'erreur | Moyen | Moyen | StatusPages générique ; en-tête Server retiré ; mode développement désactivé hors local ; /health sans version (Q9) | testApplication : 404 et 500 génériques, absence d'en-tête Server |
| Information disclosure | Secret fourni par défaut dans la config | Faible | Élevé | Aucune valeur par défaut pour un secret ; démarrage refusé si absent | Test de config : variable absente → échec explicite sans valeur |
| Denial of service | Inondation de /health | Faible | Faible | Serveur joignable seulement en local ; risque accepté en F1 (proposition) | — |
| Elevation of privilege | Endpoint de débogage ou CORS ouvert (`*`) | Faible | Moyen | Aucun endpoint hors /health ; aucun CORS en F1 (client hors ligne, Q8) | testApplication : requête cross-origin sans en-tête Access-Control-Allow-Origin |

### Menaces — Android
| Catégorie | Menace concrète | Prob. | Impact | Mesure | Test associé |
|---|---|---|---|---|---|
| Spoofing | APK de test diffusée et prise pour l'application réelle | Faible | Faible | Aucune distribution : installation par adb sur les appareils du senior ; APK jamais publiée (ni store ni releases GitHub) | — |
| Tampering | Keystore (même de débogage) commité | Faible | Moyen | `*.jks` / `*.keystore` dans .gitignore ; gitleaks | Job secrets |
| Repudiation | — | — | — | Sans objet en F1 | — |
| Information disclosure | Saisies du formulaire factice dans les sauvegardes, journaux ou miniatures | Faible | Faible | allowBackup="false" ; aucune journalisation des champs ; formulaire absent des builds de production (Q10) ; FLAG_SECURE reporté à F2 (Q15) | Lint + test UI : aucune écriture ; vérification manifeste |
| Information disclosure | Trafic en clair | Faible | Moyen | network_security_config : cleartext interdit hors debug local | Test de config / Android Lint |
| Denial of service | — | — | — | Sans objet en F1 | — |
| Elevation of privilege | Permission superflue ou composant exporté | Faible | Moyen | Seule l'Activity de lancement exportée ; aucune permission en F1, pas même INTERNET (hors ligne, Q8) ; release non débogable, R8 activé | Android Lint + test du manifeste fusionné (liste de permissions vide) |

### Menaces — web
| Catégorie | Menace concrète | Prob. | Impact | Mesure | Test associé |
|---|---|---|---|---|---|
| Spoofing | Testeur qui saisit de vrais identifiants dans le formulaire factice | Faible | Faible | Bandeau « formulaire factice, ne saisissez aucun identifiant réel » ; uniquement en local et en build de test | Capture de maquette |
| Tampering | Script tiers ou ressource CDN modifiée | Faible | Élevé | Tout servi depuis la même origine, aucun CDN ; CSP par meta `default-src 'self'; script-src 'self' 'wasm-unsafe-eval'; base-uri 'none'; form-action 'none'` (`wasm-unsafe-eval` indispensable, constaté) | Test : index.html contient la CSP ; aucune ressource externe |
| Repudiation | — | — | — | Sans objet en F1 | — |
| Information disclosure | Saisies du formulaire envoyées ou stockées (localStorage, URL) | Faible | Moyen | Aucune requête ni stockage ; aucune donnée dans l'URL | Test UI : aucun appel au port réseau ; inspection réseau manuelle |
| Information disclosure | Source maps présentes dans la distribution (constaté : 1,5 Mo) | Faible | Faible | Acceptables en local ; à exclure avant tout hébergement futur | — |
| Information disclosure | Valeur des champs exposée dans l'arbre d'accessibilité (e-mail en clair, longueur du mot de passe) lisible par un script de la page ou une extension | Faible | Faible (F1 factice) ; Moyen en F4 | Aucun script tiers (CSP) ; réévaluer en F4 avec l'issue de l'étude | Inspection de l'arbre a11y en phase 6 |
| Denial of service | Bundle Wasm lourd rendant l'app inutilisable sur réseau lent | Moyen | Moyen | Seuil de premier affichage (Q2), compression, cache | Mesure CA-6.6 |
| Elevation of privilege | Clickjacking sur le formulaire | Faible | Faible | Sans objet en local (`frame-ancestors` exige un en-tête HTTP) ; à poser avant tout hébergement futur | — |

### Menaces transverses — CI / chaîne d'approvisionnement
| Catégorie | Menace concrète | Prob. | Impact | Mesure | Test associé |
|---|---|---|---|---|---|
| Tampering | Action GitHub compromise | Faible | Élevé | Actions épinglées par SHA (fait, PR 2) ; permissions minimales | Revue ; Dependabot |
| Tampering | Node.js, Yarn ou paquet npm compromis téléchargé par la toolchain Kotlin/Wasm au build (constaté sur le prototype) | Faible | Élevé | yarn.lock commité et vérifié, audit npm en CI, versions de Node / Yarn fixées (Q18) | Job dependencies (OSV-Scanner lit yarn.lock) |

### Risques acceptés (décision du senior)
- _(à décider)_ Propositions : DoS de /health (serveur local) ; HTTP en clair
  sur localhost ; source maps et absence de `frame-ancestors` en local ;
  mesure locale de premier affichage non représentative.

### Niveau de risque proposé
- Global F1 : **Moyen** (au-dessus du « faible » pressenti) : nombreuses
  nouvelles dépendances (Gradle, npm), CSP, manifeste Android. Sans
  hébergement ni publication, le risque de déploiement disparaît, mais les
  nouvelles dépendances maintiennent le niveau Moyen.
- PR touchant shared/ : **Élevé** (règle du projet) → critical-implementer.
- PR server, composeApp, androidApp : Faible à Moyen → implementer.

## 1d. Décisions du senior et questions restantes

### Décisions (2026-11-24)
| # | Sujet | Décision |
|---|---|---|
| Q2 | Seuil de premier affichage web (critère 6) | **5 s**, métrique **First Contentful Paint**, réseau simulé « Fast 4G » de Chrome DevTools, cache vide (Q2-bis, révisé le 2026-11-24) |
| Q3 | Tests d'architecture (ADR-0002) | bibliothèque dédiée (nom, existence et maintenance vérifiés au plan) |
| Q4 | Fins de ligne | `.gitattributes` : `* text=auto eol=lf`, `gradlew` en LF, `*.bat` en CRLF |
| Q5 | Design system | base Material 3 Expressive ; mode sombre dès F1 ; propositions ci-dessous approuvées avec **P1** (sarcelle `#006A60`), **T1** (police de la plateforme), **M2** (`MotionScheme.standard()`) |
| Q6 | prototypes/ | build autonome, non inclus dans le settings.gradle.kts racine |
| Q7 | Libellés de l'écran vide | « Aucun groupe » / « Vous n'avez encore aucun groupe. » validés ; pas de bouton « Créer un groupe » en F1 |
| Q8 | Appel serveur depuis le client | aucun : application hors ligne en F1 (pas de permission INTERNET, pas de CORS) |
| Q9 | /health | statut seul ; pas de PostgreSQL en F1 (ni Flyway ni DATABASE_*) ; journal d'accès sans adresse IP |
| Q10 | Formulaire factice | builds de test uniquement, Android et web ; à la soumission, message « Formulaire factice : rien n'est envoyé. » ; seule validation : e-mail vide |
| Q1 | Hébergement et publication | aucun : localhost et APK installée par adb (application de test) |
| Q11 | applicationId et signature | `com.expensesshared` ; APK de débogage signée par la clé de débogage locale ; release construite et vérifiée en CI, jamais signée ni installée |
| Q12 | Repli JS | seulement si Wasm échoue |
| Q13 | Langue | français seul en V1 |
| Q14 | Étude a11y | menée par l'IA (session principale), aide manuelle du senior quand un geste ou un réglage l'exige (protocole ci-dessous) ; **Safari et macOS hors cible** ; gestionnaires de mots de passe testés en mode Invité / navigation privée |
| — | Risques acceptés | propositions de la STRIDE confirmées |
| Q15 | FLAG_SECURE | reporté à F2 (aucun montant en F1) |
| Q16 | Rapport d'étude | `docs/adr/0001-study-report.md` |
| Q17 | Build des maquettes | build autonome dans prototypes/01-skeleton/ |
| Q18 | Chaîne npm Wasm | `kotlin-js-store/` (yarn.lock) commité, audit npm en CI, sources acceptées |
| Q19 | Version de Compose Multiplatform | 1.11.1 |

### Q1 — Hébergement et publication
Tranchée le 2026-11-24 : **aucun serveur distant ni publication sur Google
Play** (application de test). Serveur et web lancés sur localhost, APK
installée par adb. Le point « Hébergement du backend et canal de test web »
de la carte est clos ; un hébergement futur rouvrira la STRIDE web (en-têtes
HTTP dont `frame-ancestors` et HSTS, TLS, source maps, restriction d'accès).

### Q5 — Propositions de design system (Material 3 Expressive)
Vérifié dans le cache Gradle : `org.jetbrains.compose.material3:material3:1.9.0`
contient `MaterialExpressiveTheme`, API **expérimentale**
(`@OptIn(ExperimentalMaterial3ExpressiveApi::class)`, limité au fichier de thème
du design system et signalé en PR).

- Thème : `MaterialExpressiveTheme`, schémas clair et sombre **statiques**
  (pas de couleur dynamique Android 12+, sinon la parité avec le web casse).
- Palette (une graine, schéma tonal M3 ; les paires `on*` des tons 40/100 et
  80/20 visent ≥ 4,5:1, vérifiées par un test de contraste du design system) :
  - P1 — sarcelle `#006A60` : sobre, évoque la confiance pour l'argent ;
  - P2 — indigo `#4F5BD5` : proche du prototype actuel ;
  - P3 — corail `#B4532A` : chaleureux, « entre amis ».
- Typographie : échelle M3 + styles « emphasized » d'Expressive pour les titres ;
  T1 police par défaut de chaque plateforme (rien à télécharger, rendu
  légèrement différent Android / web) ou T2 police variable embarquée
  (ex. Roboto Flex, licence OFL) pour un rendu identique, au prix de quelques
  centaines de Ko sur le web.
- Formes : coins arrondis Expressive (petit 8 dp, moyen 16 dp, grand 24 dp,
  très grand 32 dp), boutons en pilule avec morphing de forme à l'appui.
- Mouvement : M1 `MotionScheme.expressive()` (ressorts marqués) ou M2
  `MotionScheme.standard()` (plus sobre, plus sûr pour les personnes sensibles
  au mouvement ; Compose ne suit pas `prefers-reduced-motion` sur le web).
- Espacements : grille de 4 dp (4, 8, 16, 24, 32) ; marge d'écran 16 dp
  (petit) / 24 dp (grand) ; largeur de contenu max 600 dp.
- Accessibilité intégrée aux composants : cible ≥ 48 dp ; **anneau de focus**
  de 3 dp couleur `primary`, décalé de 2 dp, contraste ≥ 3:1 sur tous les
  éléments interactifs (corrige le défaut du bouton) ; encarts système gérés
  par `ScreenContainer` (corrige le bandeau sous la barre d'état).

Note sur Q2 : le FCP compte la première peinture de la page, pas l'écran
Compose ; un écran d'attente HTML dans index.html le satisfait. Le rapport
d'étude donnera aussi, à titre d'information, le temps jusqu'à la première
image Compose (≈ 4 s de téléchargement attendus sur Fast 4G). L'ADR-0001
(critère 6) sera complété avec ce seuil dans la PR du rapport d'étude.

### Q14 — Protocole de l'étude d'accessibilité (US-6)
Menée sur les écrans réels de F1 (après implémentation), pas sur le prototype.
L'IA pilote et consigne ; elle n'entend pas l'audio, donc chaque lecteur
d'écran doit **afficher ou journaliser** ce qu'il prononce.

| Critère | Outil | Comment l'IA observe | Aide manuelle du senior |
|---|---|---|---|
| 1 — Android (parité) | TalkBack, Pixel 7 Pro | texte prononcé affiché à l'écran (capture adb) ; navigation par raccourcis clavier TalkBack envoyés par adb, à défaut gestes | activer dans TalkBack › Paramètres avancés › Paramètres pour les développeurs : « Afficher la sortie vocale » ; faire les balayages si adb ne pilote pas TalkBack ; quitter le tutoriel |
| 1 — Windows | NVDA (installé par le senior) + Chrome, Firefox, Edge | journal NVDA (sortie vocale) ou Visionneuse de parole | activer le journal ; autoriser la lecture du fichier journal |
| 2 — clavier | Chrome, Firefox, Edge | navigateur intégré + captures | — |
| 3 — zoom 200 % | Chrome, Firefox, Edge | captures | — |
| 4 — gestionnaires de mots de passe | Chrome (mode Invité), Firefox (fenêtre de navigation privée, Firefox n'ayant pas de mode Invité) | identifiants fictifs uniquement ; à vérifier au début de l'étude : si le gestionnaire est désactivé dans ce mode, le critère n'est pas mesurable ainsi et le senior choisit une autre méthode | ouvrir la fenêtre Invité / privée si l'IA ne peut pas le faire |
| 5 — navigateurs | deux dernières versions de Chrome, Firefox, Edge | versions relevées dans le rapport | installer les versions manquantes |

Écart avec l'ADR-0001 : les critères 1 (VoiceOver macOS) et 5 (Safari) sont
réduits par décision du senior (Safari et macOS hors cible) ; l'ADR sera
amendé dans la PR du rapport d'étude.
| 6 — FCP ≤ 5 s | Chrome DevTools, Fast 4G, cache vide | mesure automatisée | — |

Les écrans de test n'affichent que des données fictives ; aucune capture ne
doit montrer de compte, notification ou donnée personnelle de l'appareil ou de
l'ordinateur (même règle que pour les maquettes).

### Questions restantes
- Aucune. Q14-bis tranchée le 2026-11-24 : lecteur d'écran Windows = NVDA,
  installé par le senior.

## Journal
- 2026-11-22 — Phase 1 démarrée (Opus, effort à confirmer par le senior).
  Branche feat/skeleton créée. Brouillon : stories US-1 à US-6, STRIDE par
  périmètre, questions Q1 à Q17. Maquettes en attente de Q17.
  Prochaine étape : réponses du senior, maquettes, puis GATE 1.
- 2026-11-22 — Q17 tranchée (build autonome). mockup-designer : prototype
  android + wasmJs, 40 captures web, constats a11y web (champs sans nom ni type,
  focus du bouton), CSP, poids. Émulateur inutilisable (pas d'accélération).
- 2026-11-24 — Téléphone connecté autorisé par le senior. Session principale :
  valeurs d'origine relevées, 40 captures Android, dump d'accessibilité,
  essai TalkBack (non pilotable), puis tout restauré et prototype désinstallé.
  Une capture montrant le compte réel de l'appareil a été supprimée.
  Spec complétée (1b, STRIDE, Q18, Q19). Prochaine étape : GATE 1.
- 2026-11-24 — Réponses du senior intégrées (Q2 à Q19 sauf Q14) : US-1 sans
  états chargement / erreur, formulaire en build de test seulement, /health
  statut seul, aucune permission Android, pas de CORS. Options d'hébergement
  gratuit (Q1) et propositions Material 3 Expressive (Q5) ajoutées.
  Prochaine étape : questions restantes puis GATE 1.
- 2026-11-24 — Le senior écarte serveur distant et Google Play : US-4 devient
  l'exécution locale (localhost, APK par adb), serveur limité à 127.0.0.1,
  STRIDE allégée (pas de TLS, d'hébergeur, de clé de publication ni de
  secret CI ; CSP par meta). Q1 et Play Console clos. Prochaine étape :
  questions restantes puis GATE 1.
- 2026-11-24 — Le senior fixe le critère 6 (FCP ≤ 5 s), approuve les
  propositions de design system, confirme toutes les propositions par défaut
  (Q6, Q7-bis, Q9-bis, Q10-bis, Q11-bis, risques acceptés) et confie l'étude
  a11y à l'IA avec aide manuelle (protocole Q14 ajouté). Restent Q5-bis et
  Q14-bis, puis GATE 1.
- 2026-11-24 — **GATE 1 approuvée** par le senior, avec P1 / T1 / M2.
  Safari et macOS sortis de la cible (amendement de l'ADR-0001 prévu dans la
  PR du rapport d'étude) ; gestionnaires de mots de passe testés en mode
  Invité (Chrome) / navigation privée (Firefox). Q14-bis tranchée : NVDA,
  installé par le senior. Phase 1 close, aucune question ouverte.
  Prochaine étape : phase 2 (plan technique, Opus effort élevé, mode plan),
  de préférence dans une nouvelle session qui repart de ce journal.
