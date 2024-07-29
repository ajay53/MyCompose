import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.kotlinKapt)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.daggerHilt)
}

val keysProperties = Properties()
keysProperties.load(FileInputStream(rootProject.file("keys.properties")))

android {
    namespace = "com.goazzi.mycompose"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.goazzi.mycompose"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "API_KEY", "\"" + keysProperties["yelp_api_key"] + "\"")
        }
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.androidx.compose)
//    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.coordinatorlayout)
//    implementation(libs.androidx.recyclerview)
//    implementation(libs.material)
//    implementation(libs.androidx.activity)
    implementation(libs.google.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.bundles.androidx.compose.test)
    debugImplementation(libs.bundles.androidx.compose.debug)

    //region new
//    implementation(libs.androidx.appcompat)
//    implementation(libs.material)
//    implementation(libs.androidx.constraintlayout)
//    implementation(libs.androidx.compose.ui.viewbinding)

    //    new____________
    //hilt__________
    implementation(libs.dagger.hilt)
    kapt(libs.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    //coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //glide
    implementation(libs.glide)
//    implementation(libs.glide.compiler)

    //retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.convertor)

    //moshi
    implementation(libs.squareup.moshi)
    kapt(libs.squareup.moshi.codegen)
    implementation(libs.squareup.moshi.kotlin)
//    ksp(libs.squareup.moshi.codegen)

    //room
    implementation(libs.androidx.room)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    //navigation
    implementation(libs.bundles.androidx.navigation)

    //test
    testImplementation(libs.junit)

    //androidTest
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.room.testing)

//    debugImplementation(libs.androidx.ui.tooling)
//    debugImplementation(libs.androidx.ui.test.manifest)

    annotationProcessor(libs.glide.compiler)
    annotationProcessor(libs.androidx.room.compiler)

    //endregion


//    implementation("androidx.activity:activity-ktx:1.9.0")
//    implementation("androidx.fragment:fragment-ktx:1.6.2")

    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

//    implementation("at.favre.lib:hkdf:1.1.0")

    implementation("com.google.android.gms:play-services-vision:20.1.3")

//    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
//    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
//    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2") // For JSON serialization/deserialization


//    implementation("io.reactivex.rxjava3:rxjava:3.1.1") // RxJava
//    implementation("io.reactivex.rxjava3:rxandroid:3.0.0") // RxAndroid

//    implementation("com.squareup.picasso:picasso:2.8")

    // Debug logger


//    implementation("com.android.volley:volley:1.2.1")

    //bundles______________________
    implementation(libs.bundles.androidx.lifecycle)
    implementation(libs.bundles.misc)


   /* implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)*/
}