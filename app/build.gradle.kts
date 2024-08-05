plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}


android {
    namespace = "com.proyecto.mallnav"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.proyecto.mallnav"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField ("String", "DEFAULT_SERVER_URL", "\"https://ips.navigine.com\"")
        buildConfigField ("String", "DEFAULT_USER_HASH", "\"2131-2229-C767-3B9D\"")

        ndk {
            abiFilters += setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }
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

    sourceSets {
        getByName("main") {
            jniLibs.srcDir("libs")  // Esto no debería ser necesario, pero no debería causar problemas.
        }
    }

}

dependencies {
    //navigine SDK
    implementation(files("libs/libnavigine.aar"))
    //firebase
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-analytics")
    //ui
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(libs.material)
    implementation(libs.swiperefreshlayout)
    //fragment
    implementation ("androidx.fragment:fragment:1.8.1")
    //navigation
    implementation ("androidx.navigation:navigation-fragment:2.7.7")
    implementation ("androidx.navigation:navigation-ui:2.7.7")
    //lifecycle
    implementation ("androidx.lifecycle:lifecycle-livedata:2.8.2")
    implementation ("androidx.lifecycle:lifecycle-process:2.8.2")
    //glide
    implementation ("com.github.bumptech.glide:glide:4.13.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.13.0")
    //Lottie
    implementation ("com.airbnb.android:lottie:5.0.3")
    //Volley
    implementation ("com.android.volley:volley:1.2.1")
    //support
    implementation(libs.appcompat)
    //worker
    implementation(libs.work.runtime)
    //test
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}