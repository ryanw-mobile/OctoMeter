# AI Agent Instructions

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**OctoMeter** (internal name: Kunigami) is a Kotlin Multiplatform (KMP) app for tracking UK Octopus Energy tariff rates and smart meter usage. Targets Android, Desktop (macOS/Windows/Linux), and iOS. The project is in **maintenance mode** — no new features, dependency updates and critical fixes only.

Package namespace: `com.rwmobi.kunigami`

## Build & Run Commands

```bash
# Run desktop app
./gradlew run

# Build Android debug APK
./gradlew :composeApp:assembleDebug

# Full build with all checks
./gradlew build

# iOS: open iosApp/OctoMeter.xcworkspace in Xcode
```

## Testing

```bash
# Run all unit tests (Android)
./gradlew :composeApp:testDebugUnitTest

# Run desktop tests
./gradlew :composeApp:desktopTest

# Run a single test class
./gradlew :composeApp:testDebugUnitTest --tests "com.rwmobi.kunigami.ui.viewmodels.AgileViewModelTest"

# Run a single test method
./gradlew :composeApp:testDebugUnitTest --tests "com.rwmobi.kunigami.ui.viewmodels.AgileViewModelTest.methodName"

# Android instrumented tests (Pixel 2 API 35 managed device)
./gradlew :composeApp:pixel2Api35DebugAndroidTest

# Before running tests after Room schema changes
./gradlew :composeApp:kspCommonMainKotlinMetadata
```

All tests run with timezone fixed to `Europe/London`.

## Code Quality

```bash
./gradlew lintKotlin         # Check Kotlin style
./gradlew formatKotlin       # Auto-format code
./gradlew detekt             # Static analysis
./gradlew lint               # Android lint
./gradlew :composeApp:jacocoTestReportDebug  # Code coverage report
```

CI enforces all of these — run `./gradlew check assembleDebug` to replicate the CI gate.

## Architecture

Clean Architecture with three layers in `composeApp/src/commonMain/`:

```
domain/        → Pure Kotlin: models, repository interfaces, use cases
data/          → Repository implementations, data sources (network, Room DB, cache, prefs)
ui/            → Compose screens, ViewModels, navigation, theme
di/            → Koin dependency injection modules
```

**Data flow:** Compose UI → ViewModel (StateFlow) → UseCase → Repository interface → DataSource (GraphQL/REST/Room)

**Key patterns:**
- MVVM with ViewModels exposing `StateFlow`
- Repository pattern with abstract interfaces in domain, implementations in data
- Koin for DI across all platforms
- Apollo GraphQL (primary API) + Ktor REST (secondary) — GraphQL queries live in `commonMain/graphql/`
- Room database with KSP code generation (requires `kspCommonMainKotlinMetadata` task before compilation after schema changes)

## Key Tech Stack

| Purpose | Library |
|---|---|
| UI | Compose Multiplatform + Material3 |
| Networking | Ktor 3.x + Apollo GraphQL 4.x |
| Database | AndroidX Room (KMP) |
| DI | Koin 4.x |
| Async | Kotlinx Coroutines |
| Serialization | Kotlinx Serialization |
| Logging | Kermit |
| Charting | KoalaPlot |
| Settings | Multiplatform Settings |

## Source Sets

- `commonMain` / `commonTest` — shared code and tests (primary location for logic)
- `androidMain` / `androidUnitTest` / `androidInstrumentedTest`
- `desktopMain` / `desktopTest`
- `iosMain`

## Dependency Management

All dependency versions are centralized in `gradle/libs.versions.toml`. Renovate bot handles automated dependency updates.
