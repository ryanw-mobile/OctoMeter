import org.jetbrains.compose.desktop.application.dsl.TargetFormat
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
    alias(libs.plugins.kotlinxKover)
    alias(libs.plugins.gradleKtlint)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
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
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.cio)
            implementation(libs.kotlinx.coroutines.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kermit)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.koin.core)
        }
        desktopMain.dependencies {
            implementation("org.jetbrains.skiko:skiko-awt-runtime-$skikoTarget:$skikoVersion")
            implementation(compose.desktop.currentOs)
            implementation(libs.ktor.client.cio)
            implementation(libs.koin.android)
            implementation(libs.kotlinx.coroutines.swing)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))

            implementation(libs.kotlin.test)
            implementation(libs.kotlin.test.junit)
            implementation(libs.kotlinx.coroutines.test)
            // implementation(libs.mockk)
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
                        "roctopus-${variant.versionName}-$timestamp-${variant.name}.apk"
                    output.outputFileName = outputFileName
                }
        }
    }

    namespace = "com.rwmobi.roctopus"
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
        applicationId = "com.rwmobi.roctopus"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        resourceConfigurations += setOf("en")

        testInstrumentationRunner = "com.rwmobi.roctopus.ui.test.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Bundle output filename
        val timestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())
        setProperty("archivesBaseName", "roctopus-$versionName-$timestamp")
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
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
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.rwmobi.roctopus"
            packageVersion = libs.versions.versionName.get()
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
        exclude("**/generated/**")
        include("**/kotlin/**")
    }
}

tasks.named("preBuild") {
    dependsOn(tasks.named("ktlintFormat"))
}

koverReport {
    // common filters for all reports of all variants
    filters {
        // exclusions for reports
        excludes {
            // excludes class by fully-qualified JVM class name, wildcards '*' and '?' are available
            classes(
                listOf(
                    "com.rwmobi.roctopus.RoctopusApplication",
                    "com.rwmobi.roctopus.*.*MembersInjector",
                    "com.rwmobi.roctopus.*.*Factory",
                    "com.rwmobi.roctopus.*.*HiltModules*",
                    "com.rwmobi.roctopus.data.source.local.*_Impl*",
                    "com.rwmobi.roctopus.data.source.local.*Impl_Factory",
                    "com.rwmobi.roctopus.BR",
                    "com.rwmobi.roctopus.BuildConfig",
                    "com.rwmobi.roctopus.Hilt*",
                    "com.rwmobi.roctopus.*.Hilt_*",
                    "com.rwmobi.roctopus.ComposableSingletons*",
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
                    "com.rwmobi.roctopus.di",
                    "com.rwmobi.roctopus.ui.components",
                    "com.rwmobi.roctopus.ui.destinations",
                    "com.rwmobi.roctopus.ui.navigation",
                    "com.rwmobi.roctopus.ui.previewparameter",
                    "com.rwmobi.roctopus.ui.theme",
                    "com.rwmobi.roctopus.ui.utils",
                    "androidx",
                    "dagger.hilt.internal.aggregatedroot.codegen",
                    "hilt_aggregated_deps",
                ),
            )
        }
    }
}
