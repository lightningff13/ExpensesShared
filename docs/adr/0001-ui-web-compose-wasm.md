# ADR-0001 — UI web en Compose Multiplatform Wasm, sous condition d'accessibilité

- Statut : acceptée (phase 0, 2026-11-12), à confirmer à la fin de la fonctionnalité 1
- Décideur : senior du projet

## Contexte
La parité Android / web est attendue identique et l'accessibilité RGAA / WCAG AA
est obligatoire. Compose Multiplatform web rend l'interface dans un canvas :
l'accessibilité (lecteurs d'écran, clavier, zoom), la sélection de texte et le
remplissage par gestionnaire de mots de passe dépendent d'une couche sémantique
dont la maturité doit être vérifiée.

## Décision
Interface commune Compose Multiplatform (Android + wasmJs, repli js), validée par
une étude dans la fonctionnalité 1, sur l'écran « aucun groupe » et un
formulaire e-mail / mot de passe factice.

## Critères go / no-go (tous requis)
1. NVDA (Windows) et VoiceOver (macOS) annoncent rôle, libellé et état de chaque
   élément interactif.
2. Parcours complet au clavier (Tab / Maj+Tab / Entrée / Espace), focus visible.
3. Zoom navigateur à 200 % sans perte de contenu ni de fonction.
4. Les gestionnaires de mots de passe intégrés de Chrome et Firefox détectent
   les champs e-mail / mot de passe et proposent le remplissage.
5. Fonctionnement sur les deux dernières versions de Chrome, Firefox, Safari et Edge
   (Wasm ou repli JS).
6. Temps de premier affichage acceptable : seuil à fixer par le senior.

## Alternative si no-go
Interface web DOM séparée (Kotlin/JS Compose HTML ou TypeScript + React),
consommant shared/ compilé en JS ; parité « adaptée » ; nouvel ADR.

## Conséquences
- Une seule interface à maintenir si go.
- Risque de refaire l'interface web si no-go : limité car l'étude a lieu avant F2.
