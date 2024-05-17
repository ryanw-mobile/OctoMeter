# Project Kunigami ![Gradle Build](https://github.com/ryanw-mobile/OctoMeter/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/OctoMeter/graph/badge.svg?token=fGinpoyVUp)](https://codecov.io/gh/ryanw-mobile/OctoMeter)
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

## Data Security and Privacy
First thing first: This app can run under the demo mode without requiring any credentials. 

To pull real smart meter data from your Octopus Energy account, you need to generate an API key for your account at [https://octopus.energy/dashboard/new/accounts/personal-details/api-access](https://octopus.energy/dashboard/new/accounts/personal-details/api-access). This app never asks for your Octopus customer account password, and you can always generate a new API key to invalidate the old keys. 

This app stores your API key, account number, MPAN and meter serial number using `EncryptedSharedPreferences` on Android, or the Keychain on iOS. On desktop, these credentials are currently unencrypted, but expected to do so when the library we use supports it.

## Switch to Octopus!
Switch to Octopus Energy using [this link](https://share.octopus.energy/best-shell-168), both of us will get £50 (or £100 for business).
