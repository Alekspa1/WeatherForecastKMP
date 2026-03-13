import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    id("org.jetbrains.kotlin.plugin.serialization") version "2.3.0"
    id("com.codingfeline.buildkonfig") version "0.17.1"
    id("com.google.devtools.ksp") version "2.3.0"
    id("androidx.room") version "2.8.4"
}
room {
    // Указывает Room, куда сохранять JSON-схемы базы данных
    schemaDirectory("$projectDir/schemas")
}







buildkonfig {
    packageName = "com.drag0n.weatherforecastkmp"
    objectName = "SharedConfig"

    defaultConfigs {
        val properties = Properties() // Теперь 'java.util' не нужен
        val localFile = rootProject.file("local.properties")
        if (localFile.exists()) {
            localFile.inputStream().use { properties.load(it) }
        }

        val bannerId = properties.getProperty("YANDEX_BANNER_ID") ?: ""
        val weatherKey = properties.getProperty("WEATHER_API_KEY") ?: ""

        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "YANDEX_BANNER_ID",
            bannerId
        )

        buildConfigField(
            com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING,
            "WEATHER_API_KEY",
            weatherKey
        )
    }
}


kotlin {

    applyDefaultHierarchyTemplate()
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.ktor.client.okhttp)
            // Google Play Services для геолокации
            implementation(libs.play.services.location)
            implementation(libs.mobileads)
            // Huawei Mobile Services для геолокации (если нужно поддерживать устройства Huawei)
            implementation(libs.location)

        }
        commonMain.dependencies {

            implementation(libs.androidx.room.runtime)
            // Драйвер SQLite (обязателен для KMP)
            implementation(libs.androidx.sqlite.bundled)

            implementation(libs.compose.webview.multiplatform)
            implementation(libs.material.icons.extended)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.navigation.compose)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            implementation(libs.koin.core)
            // Интеграция с Compose (для функций koinViewModel() и т.д.)
            implementation(libs.koin.compose)
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.ktor.client.okhttp)

        }

        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }


    }
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jogamp.gluegen") {
                useTarget("org.jogamp.gluegen:gluegen-rt:2.3.2")
            }
            if (requested.group == "org.jogamp.jogl") {
                useTarget("org.jogamp.jogl:jogl-all:2.3.2")
            }
        }
    }
}

android {
    namespace = "com.drag0n.weatherforecastkmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.drag0n.weatherforecastkmp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
     add("kspCommonMainMetadata", "androidx.room:room-compiler:2.8.4")

}

compose.desktop {

    application {
        from(kotlin.targets.getByName("jvm"))
        mainClass = "com.drag0n.weatherforecastkmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Msi, TargetFormat.Dmg, TargetFormat.Deb)
            packageName = "com.drag0n.weatherforecastkmp"
            packageVersion = "1.0.0"

            windows{
                shortcut = true
            }
        }
    }
}
