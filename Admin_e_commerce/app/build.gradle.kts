plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.admin_e_commerce"
    compileSdk = 35

    packagingOptions {
        exclude("META-INF/INDEX.LIST")
        exclude("META-INF/DEPENDENCIES")
    }

    defaultConfig {
        applicationId = "com.example.admin_e_commerce"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // text dimension
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.android.gms:play-services-auth-api-phone:18.0.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")

    // Auth library (Only latest version kept)
    implementation("com.google.auth:google-auth-library-oauth2-http:1.22.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Lottie
    implementation("com.airbnb.android:lottie:+")

    // Image slideshow
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    // Shimmer effect
    implementation("com.facebook.shimmer:shimmer:0.5.0@aar")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
}

//plugins {
//    alias(libs.plugins.android.application)
//    alias(libs.plugins.kotlin.android)
//    id("com.google.gms.google-services")
//}
//
//android {
//    namespace = "com.example.admin_e_commerce"
//    compileSdk = 35
//
//    packagingOptions{
//        exclude ("META- INF/DEPENDENCIES")
//    }
//
//    defaultConfig {
//        applicationId = "com.example.admin_e_commerce"
//        minSdk = 24
//        targetSdk = 34
//        versionCode = 1
//        versionName = "1.0"
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//    buildFeatures {
//        viewBinding = true
//    }
//}
//
//dependencies {
//
//    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
//    implementation(libs.androidx.constraintlayout)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//
//    //text dimension
//    implementation("com.intuit.sdp:sdp-android:1.1.0")
//    implementation("com.intuit.ssp:ssp-android:1.1.0")
//
//    // Navigation Component
//    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
//    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
//    //lifecycle
//    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
//    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
//
//    // Import the Firebase BoM
//    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
//    implementation("com.google.firebase:firebase-analytics")
//    implementation ("com.google.firebase:firebase-auth-ktx")
//    implementation("com.google.android.gms:play-services-auth-api-phone:18.0.1")
//    implementation ("com.google.android.gms:play-services-auth:20.7.0")
//    implementation("com.google.firebase:firebase-storage-ktx")
//    implementation("com.google.firebase:firebase-database-ktx")
//    implementation("com.google.firebase:firebase-messaging-ktx:23.4.0")
//
//    implementation("com.google.auth:google-auth-library-oauth2-http:1.19.0")
//    implementation("com.squareup.okhttp3:okhttp:4.12.0")
//    implementation("com.google.auth:google-auth-library-oauth2-http:1.22.0")
//
//    //Retrofit
//    // Retrofit core
//    implementation("com.squareup.retrofit2:retrofit:2.9.0")
//
//    // GSON converter for JSON serialization/deserialization
//    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//
//    // Logging interceptor (for debugging network calls)
//    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
//
//    // Optional: Coroutine support
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
//
//
//    //lottie files
//    implementation ("com.airbnb.android:lottie:+")
//
//
//    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
//
//    // shimmer effect
//    implementation ("com.facebook.shimmer:shimmer:0.5.0@aar")
//
//
//    //glide
//    implementation ("com.github.bumptech.glide:glide:4.16.0")
//    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
//
//
//
//}