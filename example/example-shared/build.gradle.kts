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
        namespace = "com.splendo.kaluga.cucumber.example"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }

    val xcf = XCFramework("ExampleShared")

    iosSimulatorArm64().binaries.framework {
        baseName = "ExampleShared"
        xcf.add(this)
    }

    iosArm64().binaries.framework {
        baseName = "ExampleShared"
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
    }
}
