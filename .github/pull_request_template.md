## Résumé
<ce qui change et pourquoi, lien vers spec / maquettes / ADR>

## Niveau de risque proposé : Faible / Moyen / Élevé
## Modèles utilisés
<implémentation : implementeur / implementeur-critique ; relecture : relecteur>
## Modules et plateformes impactés
<shared / composeApp / androidApp / server ; Android / web ; impact indirect via shared>
## Zones sensibles touchées
<auth, autorisations, données perso, crypto, requêtes en base, migrations, historique, manifeste Android…>
## Écrans
<écrans touchés, captures avant/après par plateforme, écarts avec la maquette validée et leur justification>
## Nouvelles dépendances
<nom, version, écosystème, justification, vérification d'existence>
## Tests modifiés ou contrôles désactivés
<liste + justification, ou « aucun »>
## Carte du projet
<sections mises à jour, ou « structure inchangée »>
## Leçons proposées
<pour chaque leçon : règle, origine, portée, occurrences déjà relevées,
issue recommandée (intégrer / observer / rejeter) et texte exact à ajouter
à CLAUDE.md, .claude/rules/ ou au sous-agent concerné ; ou « aucune »>
## Résultat de la pré-revue IA
<points corrigés / points ouverts pour le senior>

## Checklist sécurité et qualité
- [ ] Entrées externes validées par schéma côté serveur
- [ ] Pas de concaténation dans SQL, shell ou chemins
- [ ] Sorties échappées
- [ ] Identité ET droit d'accès vérifiés sur chaque ressource (pas d'IDOR)
- [ ] Aucun contrôle d'accès uniquement côté client
- [ ] Sessions/jetons : expiration, cookies HttpOnly/Secure/SameSite, stockage sécurisé sur mobile (Keystore)
- [ ] Aucun secret, clé ou URL interne dans code/tests/logs/maquettes
- [ ] Données personnelles minimisées et absentes des logs (RGPD)
- [ ] Erreurs sans détail technique exposé
- [ ] Dépendances vérifiées, versions fixées, licences compatibles
- [ ] Tests couvrant les cas d'échec, les mesures STRIDE et les états d'écran
- [ ] Rendu conforme à la maquette validée sur chaque plateforme, accessibilité vérifiée
- [ ] API rétrocompatible avec les versions clientes en circulation
- [ ] Aucun code de `prototypes/` dans le build de production
- [ ] Sous-agent d'implémentation cohérent avec le niveau de risque
- [ ] Carte du projet à jour
- [ ] Leçons proposées tranchées (intégrées, en observation ou rejetées)
- [ ] Montants manipulés uniquement en centimes (type Montant), somme des parts == total testée
- [ ] Toute route nouvelle ou modifiée a un test d'accès refusé (non-membre → 404, membre non autorisé → 403)
- [ ] Export CSV : cellules neutralisées contre l'injection de formules
