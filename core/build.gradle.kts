plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.core"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
}

dependencies {
    // Android / Kotlin base
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    // NETWORK (EXPORT FOR APP)
    api(libs.retrofit.v290)
    api(libs.converter.gson)
    api(libs.retrofit2.converter.jackson)
    api(libs.logging.interceptor)
    api(libs.jackson.databind)
    api(libs.jackson.module.kotlin)
    // Lifecycle (ViewModelScope…)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    // Compose + Hilt navigation
    api(libs.androidx.activity.compose)
    api(libs.androidx.hilt.navigation.compose)
    // HILT – CHỈ DÙNG NỘI BỘ
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    api(libs.androidx.runtime)
    api(libs.androidx.compose.material3)
    api(libs.material3)

    // Navigator
    api(libs.androidx.navigation.compose)
    api(libs.accompanist.navigation.animation)

    // =========================
    // Test
    // =========================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}