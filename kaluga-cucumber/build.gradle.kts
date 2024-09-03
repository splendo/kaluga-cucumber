import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `maven-publish`
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinter)
}

kotlin {
    project.tasks.withType(KotlinCompile::class) {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
            freeCompilerArgs.add("-Xexpect-actual-classes")
        }
    }

    androidTarget {
        publishLibraryVariants("release", "debug")
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Wrapper for Cucumberish"
        homepage = "https://github.com/splendo/CucumberMultiplatform"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "cucumber"
        }
        pod("Cucumberish") {
            extraOpts = listOf("-Xforeign-exception-mode", "objc-wrap")
        }
    }

    sourceSets {
        all {
            languageSettings {
                if (this@all.name.lowercase().contains("ios")) {
                    optIn("kotlinx.cinterop.ExperimentalForeignApi")
                }
            }
        }
        getByName("commonTest") {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        getByName("androidMain") {
            dependencies {
                implementation(libs.cucumber.java8)
            }
        }
    }
}

android {
    namespace = "com.splendo.kaluga.cucumber"
    compileSdk = libs.versions.androidCompileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.androidMinSdk.get().toInt()
    }
    compileOptions {
        val javaVersion = JavaVersion.toVersion(libs.versions.java.get())
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
    }
}