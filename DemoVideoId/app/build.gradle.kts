plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.facephi.demovideoid"
    compileSdk = 36

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "..."
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField( "String", "LIBRARY_VERSION", "\"${libs.versions.facephiVersion.get()}\"")

    }

    buildTypes {
        release {
            //isDebuggable = true
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.kotlinx.datetime)
    implementation(compose.ui)
    implementation(compose.preview)
    implementation(compose.material3)
    debugImplementation(compose.uiTooling)
    implementation(libs.lifecycle.viewmodel.compose)
    // Logs
    implementation(libs.napier)
    // Ktor Client Http
    //implementation(libs.ktor.client.okhttp)
    //implementation(libs.ktor.serialization.kotlinx.json)
    //implementation(libs.ktor.client.logging)

    // SDK
    implementation (libs.facephi.sdk)
    implementation (libs.facephi.videoid)
    //implementation (libs.facephi.tracking)
}