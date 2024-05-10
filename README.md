# Project Kunigame ![Gradle Build](https://github.com/ryanw-mobile/Roctopus/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/Roctopus/graph/badge.svg?token=fGinpoyVUp)](https://codecov.io/gh/ryanw-mobile/Roctopus)
## A Kotlin Multiplatform project targeting Android, iOS, Desktop.

<p><img src="cover.jpg" style="width: 100%; max-width: 1000px; height: auto;" alt="cover image" style="width: 100%; max-width: 1000px; height: auto;"></p>

This is a greenfield project without a very concrete specifications. It is under active development,
and hoping to have something to show in June 2024.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the
      folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for
  your project.

Learn more
about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
