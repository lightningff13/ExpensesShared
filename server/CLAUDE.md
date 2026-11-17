# Module server — API Ktor + PostgreSQL

- Rôle : routes REST, authentification, contrôle d'accès, persistance, historique.
- Accès BDD : Exposed DSL uniquement ; transactions explicites.
- Migrations : Flyway, src/main/resources/db/migration/V<n>__<nom>.sql ; une
  migration appliquée n'est jamais modifiée (en créer une nouvelle) ; revue senior.
- Chaque route : authentification + vérification d'appartenance au groupe et du
  rôle dans le service (pas seulement dans la route) ; test d'accès refusé dédié.
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
