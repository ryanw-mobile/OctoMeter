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

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jmailen.gradle.kotlinter.tasks.FormatTask
import org.jmailen.gradle.kotlinter.tasks.LintTask
import java.io.FileInputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlinter)
}

// Configuration
val productName = "OctoMeter"
val productApkName = "OctoMeter"
val productNameSpace = "com.rwmobi.kunigami"
val isRunningOnCI = System.getenv("CI") == "true"
val buildTimestamp = SimpleDateFormat("yyyyMMdd-HHmmss").format(Date())

android {
    namespace = productNameSpace

    setupSdkVersionsFromVersionCatalog()
    setupSigningAndBuildTypes()

    defaultConfig {
        applicationId = productNameSpace

        androidResources { localeFilters.add("en") }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17

        // Kotlinx DateTime backward compatibility
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        compose = true
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

        managedDevices {
            allDevices {
                create<ManagedVirtualDevice>("pixel2Api35") {
                    device = "Pixel 2"
                    apiLevel = 35
                    systemImageSource = "aosp-atd"
                }
            }
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            output.outputFileName.set(
                "$productApkName-${variant.name}-${libs.versions.versionName.get()}-$buildTimestamp.apk",
            )
        }
    }
}

dependencies {
    implementation(project(":composeApp"))
    implementation(compose.material3)
    implementation(compose.ui)
    implementation(compose.components.uiToolingPreview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.compose.ui.tooling)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.koin.android)
    implementation(libs.kermit)
    implementation(libs.kermit.koin)
    "baselineProfile"(project(":baselineprofile"))
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    debugImplementation(libs.leakcanary.android)

    androidTestImplementation(project.dependencies.platform(libs.compose.bom))
    androidTestImplementation(compose.components.resources)
    androidTestImplementation(libs.material3.windowsizeclass)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.uiautomator)
}

tasks.withType<Test> {
    // Set the timezone to 'Europe/London' for all tests
    jvmArgs("-Duser.timezone=Europe/London")
}

kotlinter {
    reporters = arrayOf("plain", "checkstyle", "sarif")
}

tasks.withType<LintTask>().configureEach {
    exclude { element -> element.file.path.contains("generated/") }
}

tasks.withType<FormatTask>().configureEach {
    exclude { element -> element.file.path.contains("generated/") }
}

detekt {
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    exclude { element -> element.file.path.contains("generated/") }
}

// Gradle Build Utilities
private fun ApplicationExtension.setupSdkVersionsFromVersionCatalog() {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()
    }
}
private fun ApplicationExtension.setupSigningAndBuildTypes() {
    val releaseSigningConfigName = "releaseSigningConfig"
    val isReleaseBuild = gradle.startParameter.taskNames.any {
        it.contains("Release", ignoreCase = true) ||
            it.contains("Bundle", ignoreCase = true) ||
            it.equals("build", ignoreCase = true)
    }

    signingConfigs.create(releaseSigningConfigName) {
        // Only initialise the signing config when a Release or Bundle task is being executed.
        // This prevents Gradle sync or debug builds from attempting to load the keystore,
        // which could fail if the keystore or environment variables are not available.
        // SigningConfig itself is only wired to the 'release' build type, so this guard avoids unnecessary setup.
        if (isReleaseBuild) {
            val keystorePropertiesFile = file("../../keystore.properties")

            if (isRunningOnCI || !keystorePropertiesFile.exists()) {
                println("⚠️ Signing Config: using environment variables")
                keyAlias = System.getenv("CI_ANDROID_KEYSTORE_ALIAS")
                keyPassword = System.getenv("CI_ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD")
                storeFile = file(System.getenv("KEYSTORE_LOCATION"))
                storePassword = System.getenv("CI_ANDROID_KEYSTORE_PASSWORD")
            } else {
                println("⚠️ Signing Config: using keystore properties")
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
            }
        } else {
            println("⚠️ Signing Config: not created for non-release builds.")
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
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
            signingConfig = signingConfigs.getByName(name = releaseSigningConfigName)
        }
    }
}
