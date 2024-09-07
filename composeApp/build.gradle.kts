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

import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.gradleKtlint)
    alias(libs.plugins.serialization)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.kotlinPowerAssert)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidxRoom)
    alias(libs.plugins.apollographql)
}

val productName = "OctoMeter"
val productNameSpace = "com.rwmobi.kunigami"

buildConfig {
    packageName("composeapp.kunigami")
    buildConfigField("PACKAGE_NAME", provider { "$productNameSpace" })
    buildConfigField("VERSION_NAME", provider { libs.versions.versionName.get() })
    buildConfigField("VERSION_CODE", provider { libs.versions.versionCode.get() })
    buildConfigField("GITHUB_LINK", provider { "https://github.com/ryanw-mobile/OctoMeter" })
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
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

    cocoapods {
        version = libs.versions.versionName.get()
        summary = "OctoMeter Kotlin/Native module"
        homepage = "https://github.com/ryanw-mobile/OctoMeter/"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "composeApp"
            isStatic = true
            binaryOption("bundleId", "composeApp.kunigami")
        }
    }

    // https://github.com/JetBrains/compose-multiplatform/issues/3123
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

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.androidx.test.core.ktx)
                implementation(libs.robolectric)
            }
        }

        val androidInstrumentedTest by getting {
            dependencies {
                implementation(project.dependencies.platform(libs.compose.bom))
                implementation(libs.material3.windowsizeclass.multiplatform)
                implementation(libs.androidx.junit)
                implementation(libs.androidx.espresso.core)
                implementation(libs.ui.test.junit4)
                implementation(libs.androidx.test.rules)
                implementation(libs.androidx.uiautomator)
            }
        }

        // https://kotlinlang.org/docs/multiplatform-android-layout.html#adjust-the-implementation-of-android-flavors
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        invokeWhenCreated("androidDebug") {
            dependencies {
                implementation(libs.leakcanary.android)
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
            implementation(libs.material3.windowsizeclass.multiplatform)
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
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
}

android {
    fun setOutputFileName() {
        applicationVariants.all {
            val variant = this
            variant.outputs
                .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
                .forEach { output ->
                    val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
                    val outputFileName =
                        "$productName-${variant.versionName}-$timestamp-${variant.name}.apk"
                    output.outputFileName = outputFileName
                }
        }
    }

    namespace = productNameSpace
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    signingConfigs {
        create("release") {
            val isRunningOnCI = System.getenv("CI") == "true"
            val keystorePropertiesFile = file("../../keystore.properties")

            if (isRunningOnCI) {
                println("Signing Config: using environment variables")
                keyAlias = System.getenv("CI_ANDROID_KEYSTORE_ALIAS")
                keyPassword = System.getenv("CI_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD")
                storeFile = file(System.getenv("KEYSTORE_LOCATION"))
                storePassword = System.getenv("CI_ANDROID_KEYSTORE_PASSWORD")
            } else if (keystorePropertiesFile.exists()) {
                println("Signing Config: using keystore properties")
                val properties = Properties()
                InputStreamReader(
                    FileInputStream(keystorePropertiesFile),
                    Charsets.UTF_8,
                ).use { reader ->
                    properties.load(reader)
                }

                keyAlias = properties.getProperty("alias")
                keyPassword = properties.getProperty("pass")
                storeFile = file(properties.getProperty("store"))
                storePassword = properties.getProperty("storePass")
            } else {
                println("Signing Config: skipping signing")
            }
        }
    }

    defaultConfig {
        applicationId = productNameSpace
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        resourceConfigurations += setOf("en")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        // Bundle output filename
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
        setProperty("archivesBaseName", "$productName-$versionName-$timestamp")
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            setOutputFileName()
        }

        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                ),
            )

            signingConfigs.getByName("release").keyAlias?.let {
                signingConfig = signingConfigs.getByName("release")
            }
            setOutputFileName()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += listOf(
                "META-INF/AL2.0",
                "META-INF/LGPL2.1",
                "META-INF/licenses/ASM",
            )
            pickFirsts += listOf(
                "win32-x86-64/attach_hotspot_windows.dll",
                "win32-x86/attach_hotspot_windows.dll",
            )
        }
    }

    testOptions {
        animationsDisabled = true

        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }

        managedDevices {
            devices {
                create<ManagedVirtualDevice>("pixel2Api33") {
                    device = "Pixel 2"
                    apiLevel = 33
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "$productNameSpace.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = productName
            packageVersion = libs.versions.versionName.get()
            description = "OctoMeter: Empowering Smart Electricity Usage"
            copyright = "© 2024 RW MobiMedia UK Limited. All rights reserved."
            vendor = "RW MobiMedia UK Limited"
            licenseFile.set(project.file("../LICENSE"))
            includeAllModules = true

            macOS {
                bundleID = productNameSpace
                dockName = productName
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
                menuGroup = productName
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

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    android.set(false)
    ignoreFailures.set(true)
    outputColorName.set("RED")
    reporters {
        reporter(ReporterType.PLAIN)
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.SARIF)
    }
    filter {
        exclude("**/BuildConfig.kt")
        exclude { element -> element.file.path.contains("generated/") }
        exclude("**/MainViewController.kt")
        include("**/kotlin/**")
    }
}

apollo {
    service("service") {
        packageName.set("$productNameSpace.graphql")
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

// common exclusion filters for all reports of all variants
kover.reports.filters.excludes {
    // excludes class by fully-qualified JVM class name, wildcards '*' and '?' are available
    classes(
        listOf(
            "$productNameSpace.KunigamiApplication*",
            "$productNameSpace.MainActivity*",
            "$productNameSpace.*.*MembersInjector",
            "$productNameSpace.*.*Factory",
            "$productNameSpace.data.source.local.*_Impl*",
            "$productNameSpace.data.source.local.*Impl_Factory",
            "$productNameSpace.BR",
            "$productNameSpace.BuildConfig",
            "$productNameSpace.ComposableSingletons*",
            "$productNameSpace.App*",
            "$productNameSpace.MainKt*",
            "$productNameSpace.NavigationLayoutType",
            "$productNameSpace.ui.extensions.WindowSizeClassExtensions*",
            "$productNameSpace.ui.extensions.ThrowableExtensions*",
            "$productNameSpace.ui.extensions.GenerateRandomLong*",
            "$productNameSpace.data.source.local.preferences.ProvideSettings*",
            "$productNameSpace.data.source.local.preferences.MultiplatformPreferencesStore*",
            "*Fragment",
            "*Fragment\$*",
            "*Activity",
            "*Activity\$*",
            "*.BuildConfig",
            "*.DebugUtil",
        ),
    )
    // excludes all classes located in specified package and it subpackages, wildcards '*' and '?' are available
    packages(
        listOf(
            "$productNameSpace.di",
            "$productNameSpace.ui.components",
            "$productNameSpace.ui.composehelper",
            "$productNameSpace.ui.destinations",
            "$productNameSpace.ui.navigation",
            "$productNameSpace.ui.previewparameter",
            "$productNameSpace.ui.theme",
            "$productNameSpace.ui.previewsampledata",
            "$productNameSpace.graphql.*",
            "androidx",
            "kunigami.composeapp.generated.*",
        ),
    )
}
