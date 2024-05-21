## _Project

Kunigami_ / OctoMeter: Empowering Smart Electricity
Usage<br/>![Gradle Build](https://github.com/ryanw-mobile/OctoMeter/actions/workflows/main_build.yml/badge.svg) [![codecov](https://codecov.io/gh/ryanw-mobile/OctoMeter/graph/badge.svg?token=fGinpoyVUp)](https://codecov.io/gh/ryanw-mobile/OctoMeter)

### Production-grade Kotlin Multiplatform App targeting Android, iOS, Desktop

<br />
<p><img src="app_banner_240518.webp" style="width: 100%; max-width: 1000px; height: auto;" alt="cover image" style="width: 100%; max-width: 1000px; height: auto;"></p>
<br />

This app is for Octopus Energy customers in the UK who have a smart meter installed. You can still
try out the app if you don't, because the App will run under the demo mode by default by showing
fake user data when authentication is required.

### It works for me

**This is not a sample app or a demo app**. This is a real app I developed for myself, and I use it
every day to monitor energy consumption and save money. Technically it should work for other Octopus
Energy customers. However, I am unable to imagine other scenarios such as the tariffs I do not use,
or multiple meter installations which may return data that the App cannot process. Therefore, it is
not guaranteed that it works for you when it should work for me.

This app is still in rapid development. The first release is not ready yet but is expected around
June 2024, as I will need to secure my next full-time job in the UK soon. For the same reason, this
app was intentionally built to reach production-level quality as possible, such that you can have a
better idea where I am at technically.

This README will be updated with more technical details when the app is close to release. Stay
tuned!
<br /><br />
<br /><br />

## To-do lists

Planned enhancements are logged as [issues](https://github.com/ryanw-mobile/OctoMeter/issues).

### Current limitations

There are the known issues to be sorted, since they are not affecting me, it will be queued to be
improved later:

* Properly handle dual-rate tariffs (day unit rate and night unit rates)

<br /><br />

## Some draft technical details

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

<br /><br />

## Running the app

I use Android Studio Koala for Android and Deskop apps. For iOS, I use Xcode 15.4.

* The easiest way to run on Android is to download the apk.
* To export the desktop app into MacOS distributable, execute `./gradlew packageDmg` (I don't use
  Windows)
* To run the desktop app, execute `./gradlew runReleaseDistrubutable` or just `./gradlew run`
* You can't export the Jar alone. To export the Jar for desktop. Jar doesn't comes with the native
  Skiko library so it won't run.
* iOS TestFlight is coming soon.

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

## Switch to Octopus!

Switch to Octopus Energy using [this link](https://share.octopus.energy/best-shell-168), both of us
will get £50 (or £100 for business).

<br /><br />

## License

```
   Copyright 2024 Ryan Wong, RW MobiMedia UK Limited, and open-source contributors

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

```
