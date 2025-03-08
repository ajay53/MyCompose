// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

//    #new
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.kotlinKsp) apply false
    alias(libs.plugins.daggerHilt) apply false
//    alias(libs.plugins.navigationSafeArgs) apply false
//    alias(libs.plugins.gmsGoogleServices) apply false
    alias(libs.plugins.firebaseAnalytics) apply false

//    alias(libs.plugins.androidApplication) apply false
//    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
//    alias(libs.plugins.kotlinParcelize) apply false
//    alias(libs.plugins.androidLibrary) apply false
//    alias(libs.plugins.kotlinKapt) apply false
//    alias(libs.plugins.kotlinKsp) apply false
//    alias(libs.plugins.daggerHilt) apply false
}