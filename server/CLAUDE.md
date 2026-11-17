# Module server — API Ktor + PostgreSQL

- Rôle : routes REST, authentification, contrôle d'accès, persistance, historique.
- Couches (ADR-0002) : application (cas d'utilisation, contrôle d'accès, ports)
  → dépend de shared:domain seulement ; adapters.http (routes minces) ;
  adapters.persistence (Exposed) ; config (environnement, assemblage).
- Accès BDD : Exposed DSL uniquement, dans adapters.persistence ; transactions explicites.
- Migrations : Flyway, src/main/resources/db/migration/V<n>__<nom>.sql ; une
  migration appliquée n'est jamais modifiée (en créer une nouvelle) ; revue senior.
- Chaque route : authentification, puis appel d'un cas d'utilisation qui vérifie
  l'appartenance au groupe et le rôle ; test d'accès refusé dédié sur le cas
  d'utilisation (doublons en mémoire) et sur la route (testApplication).
- Validation : règles de shared/ appliquées à toute entrée, plus bornes serveur
  (tailles, plages de montants, dates).
- Mots de passe : Argon2id (paramètres dans l'ADR dédié) ; limitation des
  tentatives par compte et par IP.
- Historique : toute modification ou suppression de dépense écrit une entrée
  (qui, quand, avant / après) dans la même transaction.
- Erreurs : réponses génériques, détails uniquement dans des logs sans données perso.
- Config : variables d'environnement (noms dans docs/architecture.md), jamais de valeur par défaut pour un secret.
- Tests : testApplication + Testcontainers PostgreSQL.
- Commandes : ./gradlew :server:test  ·  ./gradlew :server:run
