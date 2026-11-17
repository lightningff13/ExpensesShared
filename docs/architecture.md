# Carte du projet
_Dernière mise à jour : 2026-11-12 — phase 0 (aucune PR)_

## Vue d'ensemble
Carnet de dépenses partagées pour groupes d'amis (colocation, voyage, sorties) :
saisie des dépenses communes, soldes, remboursements suggérés minimisés.
Android et web avec parité identique, backend Ktor + PostgreSQL.

## Modules et plateformes
| Module | Langage | Cible | Dépend de | État |
|---|---|---|---|---|
| shared/ | Kotlin MP | android, jvm, wasmJs (+ js) | — | prévu (F1) |
| composeApp/ | Kotlin MP + Compose MP | android, wasmJs (+ js) | shared | prévu (F1) |
| androidApp/ | Kotlin | Android 10+ (API 29) | composeApp | prévu (F1) |
| server/ | Kotlin JVM (Ktor) | JVM | shared | prévu (F1) |

Graphe : androidApp → composeApp → shared ← server

## Stack et commandes
| Usage | Commande |
|---|---|
| Installer | JDK 21 + Android SDK ; ./gradlew build (à confirmer en F1) |
| Lancer en local | ./gradlew :server:run · :composeApp:wasmJsBrowserDevelopmentRun · :androidApp:installDebug |
| Lancer les maquettes | previews Android Studio / Fleet (prototypes/<feature>/) |
| Tests | ./gradlew allTests :server:test |
| Lint / types | ./gradlew detekt ktlintCheck :androidApp:lint |

## Organisation du code
| Dossier | Rôle | Point d'entrée |
|---|---|---|
| shared/ | domaine, montants, répartition, validation, DTO | — |
| composeApp/ | UI, design system, client API, entrée web | wasmJsMain/main.kt (F1) |
| androidApp/ | application Android | MainActivity (F1) |
| server/ | API, auth, persistance | Application.kt (F1) |
| prototypes/ | maquettes, exclues du build de production | — |
| docs/ | carte, specs, ADR, sécurité, maquettes, leçons | — |

## Écrans et parcours
| Écran | Stories | Maquette | Composants |
|---|---|---|---|
| _(aucun pour l'instant)_ | | | |

## Fonctionnalités × plateformes
| # | Fonctionnalité | Spec | Android | Web | État |
|---|---|---|---|---|---|
| 1 | Squelette + étude a11y Wasm | docs/specs/01-squelette.md | identique | identique | à spécifier |
| 2 | Groupes et dépenses en local | — | identique | identique | à faire |
| 3 | Soldes et remboursements | — | identique | identique | à faire |
| 4 | Comptes et authentification | — | identique | identique | à faire |
| 5 | Invitations, synchro, contrôle d'accès | — | identique | identique | à faire |
| 6 | Historique et export CSV | — | identique | identique | à faire |

## Flux principaux
- _(à compléter à partir de F2)_

## Modèle de données
_(à compléter)_ Entités pressenties : Utilisateur, Groupe, Membre (rôle),
Dépense, Part, Invitation, HistoriqueDépense.

## Zones sensibles
| Zone | Fichiers | Pourquoi |
|---|---|---|
| Domaine partagé | shared/ | touche toutes les plateformes, calculs financiers |
| Auth / sessions | server/…/auth, composeApp/…/session (F4) | identité, mots de passe, jetons |
| Contrôle d'accès | server/…/service (F5) | IDOR, fuite entre groupes |
| Migrations | server/src/main/resources/db/migration/ | schéma de production |
| Historique | server/…/historique (F6) | traçabilité, non-répudiation |
| Manifeste Android | androidApp/src/main/AndroidManifest.xml | permissions, sauvegardes, liens |

## Intégrations externes
| Service | Usage | Secrets requis (noms, jamais les valeurs) |
|---|---|---|
| PostgreSQL | persistance | DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD (noms à confirmer en F1) |

## Décisions clés
- ADR-0001 : UI web en Compose Multiplatform Wasm sous condition d'une étude d'accessibilité (docs/adr/0001-ui-web-compose-wasm.md)

## Dette et points ouverts
- Règle d'arrondi des répartitions à égalité (à qui va le reste) : à définir par le senior (F2/F3).
- FLAG_SECURE sur les écrans de montants Android : à trancher en spec.
- Design system (tokens + composants de base) et exclusion de prototypes/ du build : à créer en F1.
- Hébergement du backend et canal de test web : à définir (F1).
- Seuil de temps de premier affichage web (critère 6 de l'ADR-0001) : à fixer par le senior.
