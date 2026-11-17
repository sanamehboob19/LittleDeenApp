plugins {
    // Android Plugin
    alias(libs.plugins.android.application)

    // Dependency Injection (Hilt)
    alias(libs.plugins.hilt)

    // Kotlin Plugins
    alias(libs.plugins.kotlin.parcelize)
    id("com.google.devtools.ksp")

}

android {
    namespace = "com.ccx.islamic.kids.littledeenapp"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.ccx.islamic.kids.littledeenapp"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }
}


dependencies {

    // ==============================
    // 🧱 Core Android Libraries
    // ==============================
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


    // ==============================
    // 🧪 Testing
    // ==============================
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // ==============================
    // 🔄 Lifecycle (ViewModel + LiveData)
    // ==============================
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.activity.ktx)

    // ==============================
    // 🎨 UI / UX Libraries
    // ==============================
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.dots.indicator)
    implementation(libs.lottie)
    implementation(libs.shimmer)
    implementation(libs.power.menu)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.konfetti.xml)
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // ==============================
    // 💉 Dependency Injection (Hilt)
    // ==============================
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    // ==============================
    // 🔧 Background Work
    // ==============================
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.hilt.common)

    // animation
    implementation("com.daimajia.androidanimations:library:2.4@aar")

    // ----------------------------
    // 🗄️ Data  Networking
    // ----------------------------
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.jetbrains.kotlinx.coroutines.android)

    // ----------------------------
    // 🏠 Room Database
    // ----------------------------
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)


    // Image Cropper (Camera + Gallery)
    implementation("com.vanniktech:android-image-cropper:4.6.0")
    // animation
    implementation("com.daimajia.easing:library:2.4@aar")
    // Official Policy-Compliant YouTube Player (No API Key or copyright risk)
    // Updated for latest YouTube IFrame rules
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:13.0.0")



}