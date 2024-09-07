// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    kotlin("jvm") version "1.9.22" // or kotlin("multiplatform") or any other kotlin plugin
    kotlin("plugin.serialization") version "1.9.22"
    id("com.google.gms.google-services") version "4.4.2" apply false

    // Add the dependency for the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}