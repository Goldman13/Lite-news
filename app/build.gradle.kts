plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-android")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")

    defaultConfig {
        applicationId = "com.example.litelentanews"
        minSdkVersion(22)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures{
        viewBinding = true
    }

    buildTypes {
        getByName("release"){
            minifyEnabled(false)
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.dagger:dagger-android:${Versions.DAGGER}")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.2")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.2")
    implementation("com.google.dagger:dagger-android-support:${Versions.DAGGER}")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:${Versions.RETROFIT}")
    implementation("androidx.room:room-runtime:${Versions.ROOM}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.0-rc01")
    implementation("com.squareup.okhttp3:logging-interceptor:3.14.9")
    implementation("com.squareup.retrofit2:converter-simplexml:${Versions.RETROFIT}")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    kapt          ("com.github.bumptech.glide:compiler:4.11.0")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.1")
    kapt          ("androidx.room:room-compiler:${Versions.ROOM}")
    implementation("androidx.room:room-ktx:${Versions.ROOM}")
    kapt          ("com.google.dagger:dagger-android-processor:${Versions.DAGGER}")
    kapt          ("com.google.dagger:dagger-compiler:${Versions.DAGGER}")
    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
}