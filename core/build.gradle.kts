plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(30)
    buildToolsVersion("30.0.2")

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode(1)
        versionName("1.0")

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName("release"){
            minifyEnabled(false)
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
}

dependencies {
    implementation (fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation ("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
    implementation ("androidx.core:core-ktx:1.3.2")
    implementation ("androidx.appcompat:appcompat:1.2.0")
    implementation ("com.google.android.material:material:1.2.1")
    implementation ("com.squareup.retrofit2:retrofit:${Versions.RETROFIT}")
    implementation ("com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}")
    implementation ("com.google.dagger:dagger:${Versions.DAGGER}")
    implementation ("com.google.dagger:dagger-android:${Versions.DAGGER}")
    implementation("com.squareup.retrofit2:converter-jaxb:${Versions.RETROFIT}")
    kapt           ("com.google.dagger:dagger-android-processor:${Versions.DAGGER}")
    kapt           ("com.google.dagger:dagger-compiler:${Versions.DAGGER}")
    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
}