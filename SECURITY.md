# Politique de sécurité

> **English summary:** please report vulnerabilities privately through GitHub
> ("Security" tab → "Report a vulnerability"), never in a public issue.
> We acknowledge within 3 business days. Do not access other users' data
> and do not run destructive or denial-of-service tests.

## Versions prises en charge
| Version | Prise en charge |
|---|---|
| Branche `main` et dernière version publiée (web, Android) | ✅ |
| Versions antérieures | ❌ (mettre à jour) |

Le projet est en développement (V1) : seule la dernière version reçoit des correctifs.

## Signaler une vulnérabilité
- Utiliser le **signalement privé de GitHub** : onglet *Security* →
  *Report a vulnerability*. Ne jamais ouvrir d'issue publique, de PR ni
  de discussion pour une faille.
- Décrire : composant (Android, web, serveur), étapes de reproduction,
  impact estimé, version ou commit concerné.
- Ne pas joindre de données personnelles réelles ni de secrets ; utiliser
  des comptes de test.

## Périmètre
Inclus : application Android, application web, API du serveur, code de ce dépôt.
Exclus : services tiers (hébergeur, GitHub, stores), attaques par ingénierie
sociale, déni de service, failles exigeant un appareil déjà compromis
(root, accès physique déverrouillé).

## Règles pour les tests
- Tester uniquement avec vos propres comptes et groupes de test.
- Ne pas accéder, modifier ni supprimer les données d'autres utilisateurs ;
  s'arrêter dès qu'une donnée tierce est exposée et le signaler.
- Pas de test de charge, de déni de service ni de spam.
Une recherche menée de bonne foi dans ce cadre ne fera l'objet d'aucune poursuite
de notre part.

## Délais de traitement
| Étape | Délai visé |
|---|---|
| Accusé de réception | 3 jours ouvrés |
| Évaluation et niveau de gravité (CVSS) | 7 jours |
| Correctif — critique | 7 jours |
| Correctif — élevée | 30 jours |
| Correctif — moyenne | 90 jours |
| Correctif — faible | prochaine version |

Divulgation coordonnée : publication d'un avis de sécurité GitHub une fois le
correctif déployé, au plus tard 90 jours après le signalement sauf accord
contraire. Les auteurs du signalement sont crédités s'ils le souhaitent.

## Procédure interne : fuite de secret
1. **Révoquer et remplacer immédiatement** le secret (clé, jeton, mot de passe
   de base de données, clé de signature) ; ne pas commencer par nettoyer
   l'historique git : un secret poussé est considéré comme compromis.
2. Vérifier les journaux d'accès du service concerné depuis la date de fuite.
3. Retirer le secret du dépôt (et de l'historique si nécessaire) et des
   artefacts de build.
4. Si des données personnelles ont pu être exposées : évaluer la violation et,
   si elle présente un risque pour les personnes, la notifier à la CNIL dans
   les 72 heures (RGPD, article 33), puis informer les personnes concernées
   si le risque est élevé (article 34).
5. Analyse après incident : quel contrôle aurait dû l'arrêter (gitleaks,
   revue, garde-fous de l'IA) ; proposer une leçon dans docs/ai/lessons.md
   ou une règle.

## Procédure interne : vulnérabilité confirmée
Correctif développé dans un avis de sécurité GitHub privé (fork temporaire
privé), revue par le CODEOWNER, test de non-régression dédié, déploiement
canal par canal, puis publication de l'avis.
