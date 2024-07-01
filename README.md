# Compass App

La boussole qui pointe les monuments historiques.

<div align="center">
    <img alt="overview-app" src="./screenshots/short.gif" height="500px" />
</div>

## Description

**Compass App** est une application Android développée en Kotlin qui utilise les capacités de Mapbox pour afficher les monuments historiques à proximité. Elle utilise Compose pour l'interface utilisateur et inclut des fonctionnalités telles que la recherche de lieux, l'autocomplétion et l'affichage de cartes.

## Fonctionnalités

- Affichage des monuments historiques proches.
- Intégration avec Mapbox pour les cartes et la recherche.
- Interface utilisateur moderne basée sur Jetpack Compose.

## Prérequis

- Android Studio: Version Arctic Fox ou plus récente.
- SDK Android: API 21 ou plus récente.
- Kotlin: Version 1.5.21 ou plus récente.

## Installation

1. Cloner le dépôt
    ```bash
    git clone https://github.com/charley04310/android_compass
    cd android_compass
    ```

2. Ouvrir le projet dans Android Studio

3. Configurer l'environnement
    Assurez-vous d'avoir installé les SDKs et outils nécessaires dans Android Studio. Vous pouvez vérifier les prérequis dans le fichier `build.gradle`.

4. Construire l'application
    Synchronisez les dépendances et construisez le projet dans Android Studio.

## Utilisation

### Mode Développement

Pour démarrer l'application en mode développement, utilisez le bouton de lancement dans Android Studio ou exécutez la commande suivante :

```bash
./gradlew assembleDebug
```

### Mode Production

Pour construire l'application pour une mise en production :

```bash
./gradlew assembleRelease
```

## Architecture et Structure du Code

Voici un aperçu de la structure du projet :

```plaintext
android_compass/
├── app
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src
├── ASSETS_LICENSE
├── build.gradle.kts
├── buildscripts
│   ├── init.gradle.kts
│   └── toml-updater-config.gradle
├── debug.keystore
├── gradle
│   ├── libs.versions.toml
│   └── wrapper
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties
├── settings.gradle.kts
└── spotless
    └── copyright.kt
```