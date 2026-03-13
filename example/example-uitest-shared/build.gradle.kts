import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.library)
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.addAll("-Xexpect-actual-classes")
    }

    project.tasks.withType(KotlinCompile::class) {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
        }
    }

    android {
        namespace = "com.splendo.kaluga.cucumber.example.uitest"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    val cucumberishSimulatorFrameworks = "${rootProject.projectDir}/../kaluga-cucumber/build/cocoapods/synthetic/ios/build/Debug-iphonesimulator/Cucumberish"
    val cucumberishDeviceFrameworks = "${rootProject.projectDir}/../kaluga-cucumber/build/cocoapods/synthetic/ios/build/Debug-iphoneos/Cucumberish"

    val xcf = XCFramework("ExampleUITestShared")

    iosSimulatorArm64().binaries.framework {
        baseName = "ExampleUITestShared"
        isStatic = true
        linkerOpts("-F", cucumberishSimulatorFrameworks)
        xcf.add(this)
    }

    iosArm64().binaries.framework {
        baseName = "ExampleUITestShared"
        isStatic = true
        linkerOpts("-F", cucumberishDeviceFrameworks)
        xcf.add(this)
    }

    sourceSets {
        all {
            languageSettings {
                if (this@all.name.lowercase().contains("ios")) {
                    optIn("kotlinx.cinterop.ExperimentalForeignApi")
                }
            }
        }
        getByName("commonMain") {
            dependencies {
                api(project(":example-shared"))
                api("com.splendo.kaluga.cucumber:kaluga-cucumber:0.2.0-SNAPSHOT")
                api("com.splendo.kaluga.uitest:kaluga-uitest:0.3.0")
            }
        }
    }
}
