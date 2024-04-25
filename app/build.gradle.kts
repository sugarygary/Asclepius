plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.dicoding.asclepius"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dicoding.asclepius"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "BASE_URL", "\"https://newsapi.org/\"")
        buildConfigField("String", "API_KEY", "\"dafec79527e14373b1bb91fe3e41d2e1\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        mlModelBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //TFLite
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-metadata:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-task-vision:0.4.4")
    //Splash Screen API
    implementation("androidx.core:core-splashscreen:1.0.1")
    //uCrop
    implementation("com.github.yalantis:ucrop:2.2.8")
    //Room
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.activity:activity-ktx:1.9.0")
    ksp("androidx.room:room-compiler:2.6.1")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.moshi:moshi:1.15.1")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:1.15.1")
    //Dagger Hilt
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("com.google.dagger:hilt-android:2.51.1")
    //KTX
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    //Chrome Custom Tab
    implementation("androidx.browser:browser:1.8.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}