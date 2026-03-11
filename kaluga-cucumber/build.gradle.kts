import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.vanniktechPublish)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinter)
}

kotlin {
    project.tasks.withType(KotlinCompile::class) {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.java.get()))
        }
    }

    android {
        namespace = "com.splendo.kaluga.cucumber"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()

        withDeviceTest {
            instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }
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

mavenPublishing {
    publishToMavenCentral()

    pom {
        name.set(project.name)
        description.set("Kotlin Mulitplaform wrapper for cucumber")
        url.set("https://github.com/splendo/kaluga-cucumber")

        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("splendo")
                name.set("Splendo Consulting BV")
            }
        }
        scm {
            url.set("https://github.com/splendo/kaluga-cucumber")
        }
    }
}
