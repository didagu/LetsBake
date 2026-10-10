# LetsBake

> **Archived / legacy (2017).** This project is no longer maintained and will not build with current Android tooling without migration.

An Android baking app that lists recipes, shows their ingredients and step-by-step instructions with video playback, and includes a home-screen widget showing a recipe's ingredients.

## Features

- Recipe list with card layout (Picasso for images)
- Ingredients and steps per recipe, master/detail fragments for tablets
- Step videos via ExoPlayer
- Home-screen app widget (StackView) for ingredients
- JSON parsing with Gson

## Tech (as built)

- Java, Android Support Library 25
- compileSdk/targetSdk 25, minSdk 15
- Android Gradle Plugin 2.3.0

## Building today

To build on a modern toolchain you would need to upgrade the Gradle wrapper and AGP, replace `compile` with `implementation`, migrate Support Library to AndroidX, and update ExoPlayer to Media3.
