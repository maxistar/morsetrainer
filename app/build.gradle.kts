plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.maxistar.morsetrainer"
    compileSdk = 36
    val keystorePath = System.getenv("ANDROID_KEYSTORE")

    defaultConfig {
        applicationId = "com.maxistar.morsetrainer"
        minSdk = 18
        targetSdk = 36
        versionCode = 22
        versionName = "1.11.12"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        if (!keystorePath.isNullOrBlank()) {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
            signingConfig = signingConfigs.findByName("release")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.txt")
        }
    }

    packaging {
        resources {
            excludes += setOf("META-INF/LICENSE.md", "META-INF/LICENSE-notice.md")
        }
    }

    bundle {
        language {
            enableSplit = false
        }
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation(libs.gms.analytics)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)

    // Testing-only dependencies
    androidTestImplementation(libs.androidx.test.core.unit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.espresso.intents)
    androidTestImplementation(libs.fastlane.screengrab)

    testImplementation(libs.androidx.test.core.unit)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.test.ext.truth)
}
