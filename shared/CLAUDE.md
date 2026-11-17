# Module shared — domaine commun (ZONE SENSIBLE : risque élevé)

- Rôle : modèles, type Montant (centimes), répartitions et arrondi, calcul des soldes
  et remboursements, validation, DTO du contrat d'API.
- Cibles : android, jvm (serveur), wasmJs (+ js en repli).
- Interdit dans ce module : réseau, UI, base de données, horloge ou aléatoire
  implicites (les injecter), dépendance propre à une plateforme dans commonMain.
- expect/actual : uniquement si indispensable ; chaque actual a son test par cible.
- Tests : commonTest (kotlin.test), exécutés sur toutes les cibles.
  Règles de calcul : tests par exemples + propriétés (somme des parts == total).
- Contrat d'API : tout changement de DTO est rétrocompatible (champ ajouté
  optionnel avec valeur par défaut ; jamais de renommage ni de suppression en V1).
- Commandes : ./gradlew :shared:allTests  ·  ./gradlew :shared:detekt
- Implémentation : toujours implementeur-critique.
