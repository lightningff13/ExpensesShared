# Analyse STRIDE : <fonctionnalité>
_Une section « Menaces » par périmètre : shared, server, Android, web._

## Surface d'attaque
- Points d'entrée : <écrans, formulaires, routes API, liens d'invitation / App Links, export CSV>
- Données sensibles : <e-mail, nom, appartenance, montants et habitudes de dépense, jetons>
- Frontières de confiance : <client ↔ API, API ↔ PostgreSQL, membre ↔ non-membre, membre ↔ créateur>

## Menaces — <périmètre>
| Catégorie | Menace concrète | Probabilité | Impact | Mesure | Test associé |
|---|---|---|---|---|---|
| Spoofing (usurpation d'identité) | | | | | |
| Tampering (altération de données) | | | | | |
| Repudiation (action niée, absence de trace) | | | | | |
| Information disclosure (fuite d'informations) | | | | | |
| Denial of service (déni de service) | | | | | |
| Elevation of privilege (élévation de privilèges) | | | | | |

## Points à vérifier par périmètre
- shared : validation à la désérialisation, arrondis, actual sûrs (aléatoire, horloge).
- server : contrôle d'accès par groupe sur chaque route (IDOR), Argon2id, limitation
  des tentatives, historique dans la même transaction, logs sans données perso.
- Android : jetons en Keystore, exclusion des sauvegardes, captures d'écran,
  App Links vérifiés, HTTPS uniquement.
- Web : XSS, CSRF (cookies SameSite + jeton anti-CSRF), CSP, aucun jeton en
  localStorage, injection de formules dans le CSV.

## Risques acceptés (décision du senior)
## Niveau de risque proposé : Faible / Moyen / Élevé
