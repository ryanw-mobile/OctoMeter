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
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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
        }
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

    val skikoVersion = "0.7.70" // or any more recent version
    val skikoTarget = "$targetOs-$targetArch"

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            // tooling.preview is causing crash
            implementation(libs.compose.ui.tooling)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.splashscreen)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.androidx.security.crypto)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
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
        }
        desktopMain.dependencies {
            implementation("org.jetbrains.skiko:skiko-awt-runtime-$skikoTarget:$skikoVersion")
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.koin.jvm)
            implementation(libs.koin.compose)
            implementation(libs.themedetector)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.kotest.assertions.core)
            implementation(libs.koin.test)
        }
    }
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
                        "OctoMeter-${variant.versionName}-$timestamp-${variant.name}.apk"
                    output.outputFileName = outputFileName
                }
        }
    }

    namespace = "com.rwmobi.kunigami"
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
        applicationId = "com.rwmobi.kunigami"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        resourceConfigurations += setOf("en")

        testInstrumentationRunner = "com.rwmobi.kunigami.ui.test.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Bundle output filename
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
        setProperty("archivesBaseName", "OctoMeter-$versionName-$timestamp")
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
    }

    dependencies {
        debugImplementation(libs.leakcanary.android)
    }
}

compose.desktop {
    application {
        mainClass = "com.rwmobi.kunigami.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "OctoMeter"
            packageVersion = libs.versions.versionName.get()
            description = "OctoMeter: Empowering Smart Electricity Usage"
            copyright = "© 2024 Ryan Wong and open source contributors. All rights reserved."
            vendor = "RW MobiMedia UK Limited"
            licenseFile.set(project.file("../LICENSE"))
            includeAllModules = true

            macOS {
                bundleID = "com.rwmobi.kunigami"
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

        // Release builds can be generated but not running properly at the moment
        buildTypes.release.proguard {
            obfuscate = true
            optimize = true
            configurationFiles.from(project.file("compose-desktop.pro"))
        }
    }
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
        exclude("**/generated/**")
        exclude("**/MainViewController.kt")
        include("**/kotlin/**")
    }
}

tasks.named("preBuild") {
    dependsOn(tasks.named("ktlintFormat"))
}

tasks.withType<Test> {
    // Set the timezone to 'Europe/London' for all tests
    jvmArgs("-Duser.timezone=Europe/London")
}

dependencies {
    implementation(libs.androidx.profileinstaller)
    "baselineProfile"(project(":baselineprofile"))
}

buildConfig {
    packageName("composeapp.kunigami")
    buildConfigField("PACKAGE_NAME", provider { "com.rwmobi.kunigami" })
    buildConfigField("VERSION_NAME", provider { libs.versions.versionName.get() })
    buildConfigField("VERSION_CODE", provider { libs.versions.versionCode.get() })
    buildConfigField("GITHUB_LINK", provider { "https://github.com/ryanw-mobile/OctoMeter" })
}

kover {
    reports {
        // common filters for all reports of all variants
        filters {
            // exclusions for reports
            excludes {
                // excludes class by fully-qualified JVM class name, wildcards '*' and '?' are available
                classes(
                    listOf(
                        "com.rwmobi.kunigami.KunigamiApplication*",
                        "com.rwmobi.kunigami.MainActivity*",
                        "com.rwmobi.kunigami.*.*MembersInjector",
                        "com.rwmobi.kunigami.*.*Factory",
                        "com.rwmobi.kunigami.data.source.local.*_Impl*",
                        "com.rwmobi.kunigami.data.source.local.*Impl_Factory",
                        "com.rwmobi.kunigami.BR",
                        "com.rwmobi.kunigami.BuildConfig",
                        "com.rwmobi.kunigami.ComposableSingletons*",
                        "com.rwmobi.kunigami.App*",
                        "com.rwmobi.kunigami.MainKt*",
                        "com.rwmobi.kunigami.NavigationLayoutType",
                        "com.rwmobi.kunigami.data.repositor.DemoRestApiRepository",
                        "com.rwmobi.kunigami.ui.extensions.WindowSizeClassExtensions*",
                        "com.rwmobi.kunigami.ui.extensions.ThrowableExtensions*",
                        "com.rwmobi.kunigami.ui.extensions.GenerateRandomLong*",
                        "com.rwmobi.kunigami.ui.extensions.GenerateRandomLong*",
                        "com.rwmobi.kunigami.data.source.local.preferences.ProvideSettings*",
                        "com.rwmobi.kunigami.data.source.local.preferences.MultiplatformPreferencesStore*",
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
                        "com.rwmobi.kunigami.di",
                        "com.rwmobi.kunigami.ui.components",
                        "com.rwmobi.kunigami.ui.composehelper",
                        "com.rwmobi.kunigami.ui.destinations",
                        "com.rwmobi.kunigami.ui.navigation",
                        "com.rwmobi.kunigami.ui.previewparameter",
                        "com.rwmobi.kunigami.ui.theme",
                        "com.rwmobi.kunigami.ui.previewsampledata",
                        "androidx",
                        "dagger.hilt.internal.aggregatedroot.codegen",
                        "hilt_aggregated_deps",
                        "kunigami.composeapp.generated.resources",
                    ),
                )
            }
        }
    }
}
