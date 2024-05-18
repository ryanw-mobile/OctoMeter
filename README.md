## _Project Kunigami_ / OctoMeter: Empowering Smart Electricity Usage<br/>![Gradle Build](https://github.com/ryanw-mobile/OctoMeter/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/OctoMeter/graph/badge.svg?token=fGinpoyVUp)](https://codecov.io/gh/ryanw-mobile/OctoMeter)
### Production-grade Kotlin Multiplatform App targeting Android, iOS, Desktop

<br />
<p><img src="app_banner_240518.webp" style="width: 100%; max-width: 1000px; height: auto;" alt="cover image" style="width: 100%; max-width: 1000px; height: auto;"></p>
<br />

**This is not a sample app or a demo app**. This is a real app I developed for myself, and I use it every day. While you might find other Kotlin Multiplatform sample apps omitting everything non-essential, this app, however, aims to follow the commercial software development flow and quality. You could expect this app to meet the standards as if you _were_ a project client of RW MobiMedia Hong Kong some years ago. I am not taking new projects until I become a British Citizen a few years from now (_hopefully_).

I believe moving the same set of code from Android to Kotlin Multiplatform should not give anyone any excuses to deliver subpar code—such as hardcoding string values, not following Clean Architecture/SOLID principles, not writing any tests, or sometimes not even doing manual dependency injection. That is why I do not consider this a demo/sample app, because I do not want to rush to create an empty UI shell just to catch your attention. I use this app to save money, so I will keep improving this codebase as I gain experience in Kotlin Multiplatform development.

This app is still in rapid development. The first release is not ready yet but is expected around June 2024, as I will need to secure my next full-time job in the UK soon.

This README will be updated with more technical details when the app is close to release. Stay tuned!
<br /><br />
<hr/>
<br /><br />
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
<br /><br />
## Data Security and Privacy
First thing first: This app can run under the demo mode without requiring any credentials. 

To pull real smart meter data from your Octopus Energy account, you need to generate an API key for your account at [https://octopus.energy/dashboard/new/accounts/personal-details/api-access](https://octopus.energy/dashboard/new/accounts/personal-details/api-access). This app never asks for your Octopus customer account password, and you can always generate a new API key to invalidate the old keys. 

This app stores your API key, account number, MPAN and meter serial number using `EncryptedSharedPreferences` on Android, or the Keychain on iOS. On desktop, these credentials are currently unencrypted, but expected to do so when the library we use supports it.
<br /><br />
## Switch to Octopus!
Switch to Octopus Energy using [this link](https://share.octopus.energy/best-shell-168), both of us will get £50 (or £100 for business).
