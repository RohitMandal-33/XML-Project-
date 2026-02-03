plugins {
    alias(libs.plugins.android.application)
    // Parcelize is not used anywhere, so we keep the setup minimal
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.xmlproj"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.xmlproj"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // Modern Android projects using KSP should NOT manually add generated paths to sourceSets
    // if it causes conflicts with built-in Kotlin support. 
    // Removing manual sourceSets to let AGP and KSP handle it automatically.
}

dependencies {
    val fragmentVersion = "1.8.9"
    val navVersion = "2.7.7"
    val recyclerViewVersion = "1.3.2"
    val roomVersion = "2.7.0-alpha11"

    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.fragment:fragment:$fragmentVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}