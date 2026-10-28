plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.jtl.tubetv"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jtl.tubetv"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.leanback:leanback:1.2.0-alpha04")
    
    // ZXing für QR-Codes
    implementation("com.google.zxing:core:3.5.3")
    
    // Media3 (ExoPlayer) für Videowiedergabe
    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")

    // TDLib Android Integration (Core & KTX)
    implementation("io.github.tdlib-android:core:0.1.1")
    implementation("io.github.tdlib-android:ktx:0.1.1")
}
