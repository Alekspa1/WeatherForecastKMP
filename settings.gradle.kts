rootProject.name = "WeatherForecastKMP"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven ( "url https://developer.huawei.com/repo/" )
    }
}

dependencyResolutionManagement {
    repositories {
        google() // Уберите фильтры mavenContent, они могут мешать
        mavenCentral()
        maven("https://developer.huawei.com/repo/")

        // ПРАВИЛЬНЫЙ АДРЕС JOGAMP:
        maven("https://www.jogamp.org") {
            metadataSources {
                mavenPom()
                artifact()
            }
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":composeApp")