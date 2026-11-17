# Module androidApp — application Android

- Rôle : point d'entrée Activity, manifeste, configuration de build, cache SQLDelight.
- minSdk 29 (Android 10) ; targetSdk = dernière version stable exigée par Play.
- Manifeste : permissions minimales (INTERNET seulement en V1) ; toute nouvelle
  permission bloque la PR tant qu'elle n'est pas justifiée.
- android:allowBackup="false" ou règles d'exclusion des données et du cache ;
  FLAG_SECURE sur les écrans affichant des montants (à confirmer en spec).
- Trafic réseau : HTTPS uniquement (network_security_config, pas de cleartext
  hors build debug local).
- Liens d'invitation (App Links) : vérifiés (assetlinks.json), paramètres validés.
- Signature : clés hors dépôt, jamais lues par l'IA.
- Tests : tests instrumentés pour les actual Android et l'intégration Keystore.
- Commandes : ./gradlew :androidApp:assembleDebug  ·  ./gradlew :androidApp:lint
