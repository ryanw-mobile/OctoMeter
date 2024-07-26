## _[Project Kunigami](https://maps.app.goo.gl/cdZWxxSgx8jM5QZC7)_ / <br/>OctoMeter: Empowering Smart Electricity Usage<br/>[![Pull request](https://img.shields.io/badge/PRs-welcome-success?style=flat)](https://github.com/ryanw-mobile/OctoMeter/pulls) ![GitHub Repo stars](https://img.shields.io/github/stars/ryanw-mobile/OctoMeter?style=flat) ![Gradle Build](https://github.com/ryanw-mobile/OctoMeter/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/OctoMeter/graph/badge.svg?token=fGinpoyVUp)](https://codecov.io/gh/ryanw-mobile/OctoMeter) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/76861cc9ba88455d9c7eb1abd856b056)](https://app.codacy.com/gh/ryanw-mobile/OctoMeter/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

### Production-grade Kotlin Multiplatform App targeting Android, iOS, Desktop

<br />
<p align="center"><img src="app_banner_240518.webp" style="width: 100%; max-width: 1000px; height: auto;" alt="cover image" style="width: 100%; max-width: 1000px; height: auto;"></p>
<br />

### Made in the UK, For the UK.

> Complementary article: [Releasing my First True Kotlin Multiplatform App](https://medium.com/@callmeryan/releasing-my-first-true-kotlin-multiplatform-app-60d81e739eb0)

This app is designed for Octopus Energy customers in the UK who have a smart meter installed. If you don’t have a smart meter, you can still try out the app. It runs in demo mode by default, showing fake user data when authentication is required.

<p align="center"><img src="screenshots/240531_all_platforms_preview.webp" style="width: 100%; max-width: 1000px; height: auto;" alt="cover image" style="width: 100%; max-width: 1000px; height: auto;"></p>

### It works for me

This is a fully functional app that I developed for myself. **I use it every day** to monitor energy consumption and save money. While it should technically work for other Octopus Energy customers, I can’t guarantee it will handle all scenarios such as different tariffs or multiple meter installations. These may produce data that the app cannot process.

This is a dashboard-type app that runs on desktop (desktop-first), Android, and iOS. It supports an adaptive layout that works on mobile, tablet, and dynamic desktop window sizes. The app honours light and dark modes on all supported platforms.

The main purposes of this app are:

* Compile smart meter electricity usage; estimate and project electricity usage and cost.
* Track the Agile Octopus tariff unit rates for the coming 24 hours, with countdown and automatic refresh.
* Browse available Octopus Energy tariffs.

For non-Octopus Energy customers, or non-UK residents, the demo mode will display random meter readings and default tariff rates to illustrate the app’s functionality.

<br />
<p align="center"><img src="https://github.com/ryanw-mobile/OctoMeter/blob/main/screenshots/240603_agile_animation.gif" style="width: 100%; max-width: 1000px; height: auto;" alt="cover image" style="width: 100%; max-width: 1000px; height: auto;"></p>
<br />

The current release meets my daily needs. However, this project will continue to be maintained and improved slowly as a way for me to gain real-world hands-on Kotlin Multiplatform development experience. For this reason, the app was intentionally built to reach production-level quality as much as possible.

If you like this app, consider giving me a star, [following me on Medium](https://medium.com/@callmeryan), or [connecting with me on LinkedIn](https://www.linkedin.com/in/ryanwmobile/). 

I am also looking for my next Senior Android Dev role, too.

<br /><br />

## Switch to Octopus!

* Learn more about the [Agile Octopus](https://octopus.energy/smart/agile/) smart tariff, or other available Octopus tariffs shown in this App.
* Switch to Octopus Energy using [this link](https://share.octopus.energy/best-shell-168), both of us will get £50 (or £100 for business).

<br /><br />

## Running the app

I use Android Studio Koala to build the Android and Deskop apps. Xcode 15.4 for iOS.

All downloadables are provided under the [Release Section](https://github.com/ryanw-mobile/OctoMeter/releases).
* MacOS Desktop App: We provide a DMG installer.
* Windows Desktop App: We provide both EXE and MSI installers.
* Android: We provide signed APK and App Bundle.
* iOS: Join as a tester at [Test Flight](https://testflight.apple.com/join/T6I940RE).
* To build and run the Desktop app yourself, execute `./gradlew run`

<br /><br />

## To-do lists

Planned enhancements are logged as [issues](https://github.com/ryanw-mobile/OctoMeter/issues).

This project has entered the second development phase - Ktor RestAPI calls are being migrated to GraphQL.

[Version 1.4.0](https://github.com/ryanw-mobile/OctoMeter/tree/release/v1.4.0) is the last version it contains Ktor-RestAPI.

I have limited development budget. There are known issues which are not likely to be address for the time being due to shortage of funding.

> [!IMPORTANT]  
> **Regarding fixed charges, tariff rates, and cost estimation shown on the app:**
>This app has no access to your payment details, so by default it applies the direct debit tariff to estimate usage costs. The daily half-hourly cost should closely match the actual bill. For longer intervals, the app may not produce the exact billable amount. Please refer to your actual bills in that case.

<br /><br />

## Data Security and Privacy

First thing first: This app can run under the demo mode without requiring any credentials.

To pull real smart meter data from your Octopus Energy account, you need to generate an API key for
your account
at [https://octopus.energy/dashboard/new/accounts/personal-details/api-access](https://octopus.energy/dashboard/new/accounts/personal-details/api-access).
This app never asks for your Octopus customer account password, and you can always generate a new
API key to invalidate the old keys.

This app stores your API key, account number, MPAN and meter serial number
using `EncryptedSharedPreferences` on Android, or the Keychain on iOS. On desktop, these credentials
are currently unencrypted, but expected to do so when the library we use supports it.

<br /><br />

## Some technical details

* `/composeApp` is for Kotlin code shared across the Compose Multiplatform application.
  It contains several subfolders:
    - `commonMain` is for code that’s common for all targets.
    - `androidMain` is the traditional Android project root.
    - `desktopMain` is for the desktop (JVM) app.
    - `iosMain` is for the Kotlin code to be exposed to the iOS app.

* `/iosApp` contains the iOS application. Open `OctoMeter.xcworkspace` to build the App.

### Dependencies

* [Kermit](https://github.com/touchlab/Kermit) - Apache 2.0 - A Kotlin Multiplatform logging library
* [KoalaPlot](https://github.com/koalaplot/koalaplot-core) - MIT - A plotting library for Kotlin Multiplatform
* [AndroidX Activity Compose](https://developer.android.com/jetpack/androidx/releases/activity) - Apache 2.0 - Jetpack Compose integration with Activity
* [AndroidX Core Splashscreen](https://developer.android.com/jetpack/androidx/releases/core) - Apache 2.0 - Core splash screen
* [AndroidX Security Crypto](https://developer.android.com/jetpack/androidx/releases/security) - Apache 2.0 - Security library for Android
* [Jetpack Compose UI Tooling](https://developer.android.com/jetpack/compose/tooling) - Apache 2.0 - UI tooling for Jetpack Compose
* [AndroidX Test Core KTX](https://developer.android.com/jetpack/androidx/releases/test) - Apache 2.0 - Core KTX library for AndroidX test
* [AndroidX Room](https://developer.android.com/jetpack/androidx/releases/room) - Apache 2.0 - Room Database
* [AndroidX SQLite Bundled](https://developer.android.com/jetpack/androidx/releases/sqlite) - Apache 2.0 - Bundled SQLite for AndroidX
* [Robolectric](http://robolectric.org/) - Apache 2.0 - Unit testing framework for Android
* [Ktor](https://ktor.io/) - Apache 2.0 - Framework for building asynchronous servers and clients in connected systems
* [Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime) - Apache 2.0 - A multiplatform Kotlin library for working with date and time
* [Kotlinx Coroutines](https://github.com/Kotlin/kotlinx.coroutines) - Apache 2.0 - Libraries for Kotlin coroutines
* [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization) - Apache 2.0 - Kotlin multiplatform serialization library
* [Material3 Window Size Class Multiplatform](https://github.com/material-components/material-components-android) - Apache 2.0 - Material Design components for Jetpack Compose
* [Koin](https://insert-koin.io/) - Apache 2.0 - Dependency injection framework for Kotlin
* [LeakCanary](https://square.github.io/leakcanary/) - Apache 2.0 - A memory leak detection library for Android
* [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings) - Apache 2.0 - Multiplatform settings library for Kotlin
* [Jetpack Navigation Compose](https://developer.android.com/jetpack/androidx/releases/navigation) - Apache 2.0 - Navigation component for Jetpack Compose
* [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - Apache 2.0 - Lifecycles-aware components
* [Theme Detector](https://github.com/Dansoftowner/jSystemThemeDetector) - MIT - Detect system theme (light/dark mode)
* [AndroidX Test Ext JUnit](https://developer.android.com/jetpack/androidx/releases/test) - Apache 2.0 - Extensions for Android testing
* [AndroidX Espresso](https://developer.android.com/training/testing/espresso) - Apache 2.0 - UI testing framework
* [AndroidX UI Automator](https://developer.android.com/training/testing/ui-automator) - Apache 2.0 - UI automation testing framework
* [AndroidX Benchmark](https://developer.android.com/jetpack/androidx/releases/benchmark) - Apache 2.0 - Benchmarking library
* [AndroidX Profile Installer](https://developer.android.com/jetpack/androidx/releases/profileinstaller) - Apache 2.0 - Install profiles for faster startup
* [SLF4J](http://www.slf4j.org/) - MIT - Simple facade for logging systems
* [Skiko](https://github.com/JetBrains/skiko) - Apache 2.0 - Kotlin Multiplatform bindings to Skia
* [Apollo GraphQL](https://www.apollographql.com/docs/android/) - MIT - A strongly-typed, caching GraphQL client for Android and Kotlin Multiplatform

### Plugins

* [Android Application Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) - Google - Plugin for building Android applications
* [Android Library Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) - Google - Plugin for building Android libraries
* [KSP Plugin](https://github.com/google/ksp) - Google - Kotlin Symbol Processing API
* [Android Test Plugin](https://developer.android.com/studio/build/gradle-plugin-3-0-0-migration) - Google - Plugin for Android test projects
* [AndroidX Room Plugin](https://developer.android.com/jetpack/androidx/releases/room) - Google - Plugin for AndroidX Room
* [Jetbrains Compose Plugin](https://github.com/JetBrains/compose-jb) - JetBrains - Plugin for Jetpack Compose
* [Compose Compiler Plugin](https://developer.android.com/jetpack/compose) - JetBrains - Plugin for Jetpack Compose
* [Kotlin Multiplatform Plugin](https://kotlinlang.org/docs/multiplatform.html) - JetBrains - Plugin for Kotlin Multiplatform projects
* [Kotlin CocoaPods Plugin](https://kotlinlang.org/docs/native-cocoapods.html) - JetBrains - Plugin for integrating with CocoaPods
* [Kotlin Power Assert Plugin](https://kotlinlang.org/docs/reference/power-assert.html) - JetBrains - Plugin for enhanced assertions in Kotlin
* [Ktlint Plugin](https://github.com/JLLeitschuh/ktlint-gradle) - JLLeitschuh - Plugin for Kotlin linter
* [Kover Plugin](https://github.com/Kotlin/kotlinx-kover) - JetBrains - Code coverage tool for Kotlin
* [Serialization Plugin](https://github.com/Kotlin/kotlinx.serialization) - JetBrains - Plugin for Kotlin serialization
* [BuildConfig Plugin](https://github.com/gmazzo/gradle-buildconfig-plugin) - gmazzo - Plugin for generating BuildConfig classes
* [Baseline Profile Plugin](https://developer.android.com/studio/profile/baselineprofile) - AndroidX - Plugin for generating baseline profiles
* [Apollo GraphQL Plugin](https://www.apollographql.com/docs/android/advanced/gradle-plugin/) - Apollo GraphQL - Plugin for integrating Apollo GraphQL client with Android and Kotlin projects

<br /><br />

## Licenses

This project is licensed under the Mozilla Public License Version 2.0 (MPL-2.0) with a non-commercial clause. See the [LICENSE](./LICENSE) file for details.

### Icon licenses

Every tiny piece matters. This App contains the icons contributed by:

**MIT License:**
* [Bootstrap Icons](https://github.com/twbs/icons)
* [Eva Icons](https://github.com/akveo/eva-icons/)
* [Lineicons](https://github.com/LineiconsHQ/Lineicons)
* [Fluent UI System Icons](https://github.com/microsoft/fluentui-system-icons)
* [Fluent UI React](https://github.com/microsoft/fluentui/tree/master/packages/react-icons-mdl2)
* [Phosphor Icons](https://github.com/phosphor-icons/core)
* [Tabler Icons](https://github.com/tabler/tabler-icons)
* [akar-icons](https://github.com/artcoholic/akar-icons)

**Apache 2.0 License:**
* [Material Design](https://github.com/Templarian/MaterialDesign)
* [Material Design Icons](https://github.com/material-icons/material-icons)

**CC BY 4.0 License:**
* [Basil Icons](https://www.figma.com/community/file/931906394678748246/basil-icons)
* [Font Awesome](https://github.com/FortAwesome/Font-Awesome)
* [Solar Icons Set](https://www.figma.com/community/file/1166831539721848736)
* [Streamline](http://streamlinehq.com)

**ISC License:**
* [Lucide](https://github.com/lucide-icons/lucide)
