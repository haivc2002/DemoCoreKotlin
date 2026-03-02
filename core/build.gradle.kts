plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
}

kapt {
    correctErrorTypes = true
}

//dependencies {
//    coreLibraryDesugaring(libs.desugar.jdk.libs)
//    // Android / Kotlin base
//    api(libs.androidx.core.ktx)
//    api(libs.androidx.appcompat)
//    api(libs.material)
//    // NETWORK (EXPORT FOR APP)
//    api(libs.retrofit.v290)
//    api(libs.converter.gson)
//    api(libs.retrofit2.converter.jackson)
//    api(libs.logging.interceptor)
//    api(libs.jackson.databind)
//    api(libs.jackson.module.kotlin)
//    // Lifecycle (ViewModelScope…)
//    api(libs.androidx.lifecycle.viewmodel.ktx)
//    // Compose + Hilt navigation
//    api(libs.androidx.activity.compose)
//    api(libs.androidx.hilt.navigation.compose)
//    // HILT – CHỈ DÙNG NỘI BỘ
//    implementation(libs.hilt.android)
//    kapt(libs.hilt.android.compiler)
//
//    api(libs.androidx.runtime)
//    api(libs.androidx.compose.material3)
//    api(libs.material3)
//    // Navigator
//    api(libs.androidx.navigation.compose)
//    api(libs.accompanist.navigation.animation)
//    api(platform(libs.androidx.compose.bom.v20241201))
//    api(libs.androidx.compose.material.icons.extended)
//    api(libs.androidx.datastore.preferences)
//    api("androidx.compose.material:material")
//
//    // =========================
//    // Test
//    // =========================
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//}

dependencies {
    // =========================
    // Core
    // =========================
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    api(libs.androidx.core.ktx)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.androidx.compose.material)
    // =========================
    // Network
    // =========================
    api(libs.retrofit.v290)
    api(libs.converter.gson)
    api(libs.retrofit2.converter.jackson)
    api(libs.logging.interceptor)
    api(libs.jackson.databind)
    api(libs.jackson.module.kotlin)
    // =========================
    // Lifecycle
    // =========================
    api(libs.androidx.lifecycle.viewmodel.ktx)
    // =========================
    // 🔥 COMPOSE (BẮT BUỘC BOM TRÊN CÙNG)
    // =========================
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.activity.compose)
    api(libs.androidx.navigation.compose)
    api(libs.accompanist.navigation.animation)
    api(libs.androidx.compose.material.icons.extended)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.material3)
    // =========================
    // HILT (internal)
    // =========================
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    api(libs.androidx.hilt.navigation.compose)
    // =========================
    // DataStore
    // =========================
    api(libs.androidx.datastore.preferences)

    // =========================
    // Test
    // =========================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}