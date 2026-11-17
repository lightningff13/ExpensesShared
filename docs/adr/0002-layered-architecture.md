# ADR-0002 — Architecture en couches (Clean Architecture pragmatique)

- Statut : acceptée (2026-11-17)
- Décideur : senior du projet
- Liée à : ADR-0001 (l'UI web doit pouvoir être remplacée sans toucher au domaine)

## Contexte
Le découpage de la phase 0 respecte le sens des dépendances entre modules
(shared ne dépend de rien), mais ne définit pas de couches à l'intérieur :
shared mélange domaine et DTO sérialisables, aucun emplacement n'est prévu
pour les cas d'utilisation ni pour le contrôle d'accès, et les services
appelleraient Exposed / Ktor directement, ce qui empêche de tester les règles
sensibles (autorisations, arrondis, historique) sans base ni réseau.

## Décision
Clean Architecture pragmatique : les dépendances pointent vers le domaine ;
frontières entre modules imposées par Gradle, frontières internes par
paquets + tests d'architecture.

| Unité | Couche | Contenu | Peut dépendre de |
|---|---|---|---|
| :shared:domain | Entités | Amount, Group, Member, Expense, Share, règles de répartition et d'arrondi, soldes, remboursements, erreurs métier (sealed) | stdlib Kotlin uniquement |
| :shared:contract | Adaptateur (contrat) | DTO @Serializable, mappers DTO ↔ domaine, version de l'API | domain, kotlinx.serialization |
| server › application | Cas d'utilisation + ports | un cas d'utilisation par action (ex. AddExpense), contrôle d'accès inclus ; interfaces de dépôts, horloge, générateur d'identifiants | domain |
| server › adapters.http | Adaptateur | routes Ktor minces : décoder → mapper → appeler le cas d'utilisation → mapper le résultat en code HTTP | application, domain, contract |
| server › adapters.persistence | Adaptateur | implémentations Exposed des dépôts, migrations Flyway | application, domain |
| server › config | Racine de composition | lecture de l'environnement, assemblage manuel des dépendances | tout |
| composeApp › presentation | Adaptateur (UI) | écrans, view-models, design system | composeApp.application, domain |
| composeApp › application | Cas d'utilisation client + ports | cas minces (orchestration), interfaces de dépôts client | domain |
| composeApp › data | Adaptateur | client Ktor, cache SQLDelight (androidMain), implémentations des dépôts | application, domain, contract |
| androidApp, composeApp/wasmJsMain | Racines de composition | points d'entrée, assemblage manuel | tout |

Règles complémentaires :
- Le contrôle d'accès (membre du groupe, créateur, auteur de la dépense) est
  vérifié dans le cas d'utilisation serveur, jamais seulement dans la route
  ni dans le client.
- Les cas d'utilisation renvoient un résultat typé (sealed) ; les adaptateurs
  HTTP le traduisent : non-membre → 404, interdit → 403, invalide → 422.
- Aucune annotation de framework (@Serializable, Exposed, Compose) dans
  :shared:domain ni dans les couches application.
- Injection de dépendances manuelle (pas de framework en V1).
- Noms de fichiers, paquets et identifiants en anglais (leçon L-1).

## Tests
- domain : tests unitaires et de propriétés dans commonTest, toutes cibles.
- application (serveur et client) : tests avec doublons en mémoire des ports,
  dont chaque mesure STRIDE d'autorisation.
- adapters : persistence avec Testcontainers PostgreSQL, http avec
  testApplication, data client avec MockEngine Ktor.
- contract : tests d'aller-retour JSON et de rétrocompatibilité.

## Application des règles
- Entre modules : par construction (dépendances Gradle).
- Dans server et composeApp : tests d'architecture vérifiant les imports
  interdits ; outil à choisir dans le plan de la fonctionnalité 1 (règle
  detekt ou bibliothèque dédiée, existence et maintenance vérifiées).
  En attendant, contrôle par le reviewer.

## Alternatives écartées
- Module shared unique (phase 0) : mélange entités et format de transport.
- Couches en modules Gradle séparés côté serveur et client : contrainte plus
  forte mais coût de configuration trop élevé pour la taille du projet ;
  à reconsidérer si les tests d'architecture s'avèrent insuffisants.
- Cas d'utilisation partagés dans shared : les cas sensibles (autorisation,
  persistance, historique) sont propres au serveur ; côté client ils restent minces.

## Conséquences
- Plus de fichiers et de mappers ; squelette (F1) légèrement plus gros.
- Règles sensibles testables isolément ; UI web remplaçable sans toucher
  au domaine ni aux cas d'utilisation (repli de l'ADR-0001).
