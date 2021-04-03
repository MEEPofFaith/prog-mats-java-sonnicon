![Logo](assets/icon.png)

[![Java CI with Gradle](https://github.com/MEEPofFaith/prog-mats-java/workflows/Java%20CI%20with%20Gradle/badge.svg)](https://github.com/sMEEPofFaith/prog-mats-java/actions) [![Discord](https://img.shields.io/discord/704355237246402721.svg?logo=discord&logoColor=white&logoWidth=20&labelColor=7289DA&label=Discord)](https://discord.gg/RCCVQFW) [![Stars](https://img.shields.io/github/stars/MEEPofFaith/prog-mats-java?label=Star%20the%20mod%20here%21&style=social)]()

# Progressed Materials
(I definitely didn't steal this README format from BetaMindy)

Progressed Materials, but now has been ported to Java, and all subsequent work will be in Java.
Adds a bunch of ~~overly animated~~ turrets that probably aren't balanced because I suck at balancing.
If you want to play the old JS version for some reason, install `MEEPofFaith/prog-mats-js`. The `modName` on that is the same as this, so do not install both the JS and Java versions or there will be conflicts.

## Download Now!   
[![Download](https://img.shields.io/github/v/release/MEEPofFaith/prog-mats-java?color=gold&include_prereleases&label=DOWNLOAD%20LATEST%20RELEASE&logo=github&logoColor=FCC21B&style=for-the-badge)](https://github.com/MEEPofFaith/prog-mats-java/releases)

__Or find it in the in-game mod browser, were installation is automatic and you can ignore everything under this message!__

### Releases   
Go to the releases, the latest release will have a `ProgressedMaterials.jar` attached to it that you can download. If it does not have it, follow the steps below(recommended) or bother me with a new issue so I can attach the compiled mod.   
After you have the `ProgressedMaterials.jar`, paste it into your mod folder (locate your mod folder in the "open mod folder" of Mindustry).

### Bleeding Edge Builds?
(Just gonna steal this term from Mindustry don't mind me)

Go to the Actions tab and open the latest workflow with a green ☑️ on it. The zip file at the bottom will contain the latest build of Progressed Materials, though due to it being still in deveopement and my own stupidity you probably shouldn't do that. Anyways unzip that zip to get a `ProgressedMaterials.jar` file and follow the instal steps in **Releases**.

## Compiling
JDK 8.

### Windows
Plain Jar: `gradlew build`\
Dexify Plain Jar: `gradlew dexify`\
Build Plain & Dexify Jar: `gradlew buildDex`

### *nix
Plain Jar: `./gradlew build`\
Dexify Plain Jar: `./gradlew dexify`\
Build Plain & Dexify Jar: `./gradlew buildDex`

Plain Jar is for JVMs (desktop).\
Dexed Jar is for ARTs (Android). This requires `dx` on your path (Android build-tools).\
These two are separate in order to decrease size of mod download.
