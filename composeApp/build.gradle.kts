/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    id("jacoco")
    alias(libs.plugins.serialization)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinPowerAssert)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidxRoom)
    alias(libs.plugins.apollographql)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

// Configuration
val productNameSpace = "com.rwmobi.kunigami"

buildConfig {
    packageName("composeapp.kunigami")
    buildConfigField("PACKAGE_NAME", provider { "$productNameSpace" })
    buildConfigField("VERSION_NAME", provider { libs.versions.versionName.get() })
    buildConfigField("VERSION_CODE", provider { libs.versions.versionCode.get() })
    buildConfigField("GITHUB_LINK", provider { "https://github.com/ryanw-mobile/OctoMeter" })
}

kotlin {
    android {
        namespace = "$productNameSpace.shared"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        androidResources {
            enable = true
        }

        withHostTestBuilder {}.configure {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }

        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }.configure {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    jvm("desktop")
    jvmToolchain(17)

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // https://youtrack.jetbrains.com/issue/CMP-3123
    val osName = System.getProperty("os.name")
    val targetOs = when {
        osName == "Mac OS X" -> "macos"
        osName.startsWith("Win") -> "windows"
        osName.startsWith("Linux") -> "linux"
        else -> error("Unsupported OS: $osName")
    }

    val targetArch = when (val osArch = System.getProperty("os.arch")) {
        "x86_64", "amd64" -> "x64"
        "aarch64" -> "arm64"
        else -> error("Unsupported arch: $osArch")
    }

    val skikoVersion = libs.versions.skiko
    val skikoTarget = "$targetOs-$targetArch"

    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }

        androidMain.dependencies {
            // tooling.preview is causing crash
            runtimeOnly(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.compose.ui.tooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.security.crypto)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
        }

        val androidHostTest by getting {
            dependencies {
                implementation(libs.androidx.test.core.ktx)
                implementation(libs.robolectric)
            }
        }

        commonMain.dependencies {
            runtimeOnly(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.navigation.compose)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.material3.windowsizeclass)
            implementation(libs.kermit)
            implementation(libs.kermit.koin)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.koalaplot.core)
            implementation(libs.multiplatform.settings)
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.apollo.runtime)
            implementation(libs.apollo.adapters.core)
        }

        val desktopMain by getting {
            // To provide RoomDB Ctor actual declaration
            kotlin.srcDir("build/generated/ksp/metadata")
            dependencies {
                runtimeOnly("org.jetbrains.skiko:skiko-awt-runtime-$skikoTarget:$skikoVersion")
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.koin.jvm)
                implementation(libs.koin.compose)
                implementation(libs.themedetector)
                implementation(libs.slf4j)
            }
        }

        iosMain {
            // To provide RoomDB Ctor actual declaration
            kotlin.srcDir("build/generated/ksp/metadata")
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.koin.test)
            implementation(libs.apollo.testing.support)
            implementation(libs.apollo.mockserver)
        }
    }
}

dependencies {
    "kspAndroid"(libs.androidx.room.compiler) // For AndroidUnitTest
    "kspCommonMainMetadata"(libs.androidx.room.compiler)
}

compose.resources {
    // The generated Res class defaults to internal visibility, which composeApp's own androidMain
    // code doesn't need, but androidApp (a separate module depending on composeApp) does.
    publicResClass = true
}

compose.desktop {
    application {
        mainClass = "$productNameSpace.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "OctoMeter"
            packageVersion = libs.versions.versionName.get()
            description = "OctoMeter: Empowering Smart Electricity Usage"
            copyright = "© 2024-2025 RW MobiMedia UK Limited. All rights reserved."
            vendor = "RW MobiMedia UK Limited"
            licenseFile.set(project.file("../LICENSE"))
            includeAllModules = true

            macOS {
                bundleID = productNameSpace
                dockName = "OctoMeter"
                iconFile.set(project.file("icons/ic_launcher_macos.icns"))
                notarization {
                    val providers = project.providers
                    appleID.set(providers.environmentVariable("NOTARIZATION_APPLE_ID"))
                    password.set(providers.environmentVariable("NOTARIZATION_PASSWORD"))
                    teamID.set(providers.environmentVariable("NOTARIZATION_TEAM_ID"))
                }
            }
            windows {
                iconFile.set(project.file("icons/ic_launcher_windows.ico"))
                menuGroup = "OctoMeter"
                shortcut = true
                dirChooser = true
                perUserInstall = true
                upgradeUuid = "4af5f6a6-3fbe-465b-af40-549cd7a9c09c"
            }
            linux {
                iconFile.set(project.file("icons/ic_launcher_linux.png"))
            }
        }

        buildTypes.release.proguard {
            obfuscate = true
            optimize = true
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
}

tasks.withType<Test> {
    // Set the timezone to 'Europe/London' for all tests
    jvmArgs("-Duser.timezone=Europe/London")
}

kotlinter {
    reporters = arrayOf("plain", "checkstyle", "sarif")
}

tasks.withType<LintTask>().configureEach {
    exclude("**/BuildConfig.kt")
    exclude { element -> element.file.path.contains("generated/") }
    exclude("**/MainViewController.kt")
}

tasks.withType<FormatTask>().configureEach {
    exclude("**/BuildConfig.kt")
    exclude { element -> element.file.path.contains("generated/") }
    exclude("**/MainViewController.kt")
}

detekt {
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    exclude { element -> element.file.path.contains("generated/") }
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    // include("**/special/package/**") // only analyze a sub package inside src/main/kotlin
    exclude { element -> element.file.path.contains("generated/") }
}

apollo {
    service("service") {
        packageName.set("$productNameSpace.graphql")
        mapScalarToKotlinDouble(graphQLName = "Decimal")
        mapScalar(graphQLName = "DateTime", "kotlin.time.Instant")
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
powerAssert {
    functions.addAll(
        "kotlin.assert",
        "kotlin.test.assertTrue",
        "kotlin.test.assertEquals",
        "kotlin.test.assertNull",
        "kotlin.require",
    )
}

jacoco {
    toolVersion = "0.8.14"
}

tasks.register<JacocoReport>("jacocoTestReportDebug") {
    dependsOn("testAndroidHostTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        // Excluded classes
        "**/*MembersInjector.class",
        "**/*Factory.class",
        "**/data/source/local/*_Impl*.class",
        "**/data/source/local/*Impl_Factory.class",
        "**/BR.class",
        "**/BuildConfig.class",
        "**/ComposableSingletons*.class",
        "**/App*.class",
        "**/NavigationLayoutType.class",
        "**/ui/extensions/WindowSizeClassExtensions*.class",
        "**/ui/extensions/ThrowableExtensions*.class",
        "**/ui/extensions/GenerateRandomLong*.class",
        "**/data/source/local/preferences/ProvideSettings*.class",
        "**/data/source/local/preferences/MultiplatformPreferencesStore*.class",
        "**/*Fragment.class",
        "**/*Fragment\$*.class",
        "**/*Activity.class",
        "**/*Activity\$*.class",
        "**/DebugUtil.class",
        // Excluded packages
        "**/com/rwmobi/kunigami/di/**",
        "**/com/rwmobi/kunigami/ui/components/**",
        "**/com/rwmobi/kunigami/ui/composehelper/**",
        "**/com/rwmobi/kunigami/ui/destinations/**",
        "**/com/rwmobi/kunigami/ui/navigation/**",
        "**/com/rwmobi/kunigami/ui/previewparameter/**",
        "**/com/rwmobi/kunigami/ui/theme/**",
        "**/com/rwmobi/kunigami/ui/previewsampledata/**",
        "**/com/rwmobi/kunigami/graphql/**",
        "**/androidx/**",
        "**/kunigami/composeapp/generated/**",
    )

    classDirectories.setFrom(
        layout.buildDirectory.dir("classes/kotlin/android/main").map { dir ->
            fileTree(dir) {
                exclude(fileFilter)
            }
        },
    )
    sourceDirectories.setFrom(
        files(
            "src/commonMain/kotlin",
            "src/androidMain/kotlin",
        ),
    )
    executionData.setFrom(
        layout.buildDirectory.dir("jacoco").map { dir ->
            fileTree(dir).include("*.exec")
        },
    )
}
