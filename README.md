![Logo](assets/icon.png)

[![Java CI with Gradle](https://github.com/MEEPofFaith/prog-mats-java/workflows/Java%20CI%20with%20Gradle/badge.svg)](https://github.com/MEEPofFaith/prog-mats-java/actions) [![Discord](https://img.shields.io/discord/704355237246402721.svg?logo=discord&logoColor=white&logoWidth=20&labelColor=7289DA&label=Discord)](https://discord.gg/RCCVQFW) [![Discord](https://img.shields.io/discord/704355237246402721.svg?logo=discord&logoColor=white&logoWidth=20&labelColor=8a1a1a&label=Avant)](https://discord.gg/V6ygvgGVqE) [![Stars](https://img.shields.io/github/stars/MEEPofFaith/prog-mats-java?label=Star%20the%20mod%20here%21&style=social)]()

# Progressed Materials
(I definitely didn't steal this README format from BetaMindy)

Progressed Materials, but now has been ported to Java, and all subsequent work will be in Java. Progressed Materials is a combination of three of my previous v5 mods:
- Ohno Missiles
- Extra Sand in the Sandbox
- Progressed Materials

Add a load of turrets ~~many of which need balancing~~, small minor things, and sandbox only items for fun and testing.

## Download Now!   
[![Download](https://img.shields.io/github/v/release/MEEPofFaith/prog-mats-java?color=gold&include_prereleases&label=DOWNLOAD%20LATEST%20RELEASE&logo=github&logoColor=FCC21B&style=for-the-badge)](https://github.com/MEEPofFaith/prog-mats-java/releases)

__Or find it in the in-game mod browser, where installation is almost completely automatic and you can ignore everything under this message!__

### Releases   
Go to the releases, the latest release will have a `ProgMatsDexed.jar` attached to it that you can download. If it does not have it, follow the steps below(recommended) or bother me with a new issue so I can attach the compiled mod.   
After you have the `ProgMatsDexed.jar`, paste it into your mod folder (locate your mod folder in the "open mod folder" of Mindustry).

## Compiling Yourself
JDK 15 (I think)

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
