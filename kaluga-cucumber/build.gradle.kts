import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    `maven-publish`
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
}

kotlin {
    targets.configureEach {
        compilations.configureEach {
            kotlinOptions.freeCompilerArgs += "-Xexpect-actual-classes"
        }
    }

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
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
                implementation("io.cucumber:cucumber-java8:4.8.1")
            }
        }
    }
}

android {
    namespace = "com.splendo.kaluga.cucumber"
    compileSdk = 34
    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

android {
    compileOptions {
        sourceCompatibility =  JavaVersion.VERSION_11
        targetCompatibility =  JavaVersion.VERSION_11
    }
}