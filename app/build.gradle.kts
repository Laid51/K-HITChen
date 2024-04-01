plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = 34

    defaultConfig {
        applicationId = "fr.isen.OusseniLaid.androiderestaurant"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "fr.isen.OusseniLaid.androiderestaurant"
}

dependencies {
    implementation ("io.coil-kt:coil-compose:1.4.0")
    implementation ("!com.google.android.material:material:1.4.0")
    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.activity:activity-compose:1.3.1")
    implementation("androidx.compose.ui:ui:1.1.0-alpha06")
    implementation("androidx.compose.ui:ui-graphics:1.1.0-alpha06")
    implementation("androidx.compose.ui:ui-tooling-preview:1.1.0-alpha06")
    implementation("androidx.compose.material3:material3:1.0.0-alpha05")
    implementation("androidx.compose.ui:ui-tooling:1.1.0-alpha06")
    implementation("com.android.volley:volley:1.2.1")
    implementation(libs.androidx.material3.android)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.0-alpha06")
    debugImplementation("androidx.compose.ui:ui-tooling:1.1.0-alpha06")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.1.0-alpha06")
}
