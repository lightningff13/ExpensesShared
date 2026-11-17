# Module shared — domaine et contrat (ZONE SENSIBLE : risque élevé)

Deux modules Gradle, couches décrites dans l'ADR-0002.

## shared/domain (:shared:domain) — entités
- Contenu : Amount (centimes), Group, Member, Expense, Share, répartitions et
  arrondi, soldes, remboursements, erreurs métier (sealed).
- Dépendances : stdlib Kotlin uniquement (toute exception passe par un ADR).
- Interdit : sérialisation, réseau, UI, base de données, horloge ou aléatoire
  implicites (les injecter), annotations de framework.

## shared/contract (:shared:contract) — contrat d'API
- Contenu : DTO @Serializable, mappers DTO ↔ domaine, version de l'API.
- Dépend de : shared:domain, kotlinx.serialization.
- Validation à la désérialisation ; tout changement rétrocompatible (champ
  ajouté optionnel avec valeur par défaut ; jamais de renommage ni de
  suppression en V1) ; tests d'aller-retour JSON.

## Commun
- Cibles : android, jvm (serveur), wasmJs (+ js en repli).
- Aucune dépendance propre à une plateforme dans commonMain.
- expect/actual : uniquement si indispensable ; chaque actual a son test par cible.
- Tests : commonTest (kotlin.test), toutes cibles ; règles de calcul par
  exemples + propriétés (somme des parts == total).
- Commandes : ./gradlew :shared:domain:allTests :shared:contract:allTests · ./gradlew detekt
- Implémentation : toujours critical-implementer.
