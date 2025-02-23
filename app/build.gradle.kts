plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.duocuc.docuvoiceapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.duocuc.docuvoiceapp"
        minSdk = 30
        targetSdk = 34
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation ("com.google.firebase:firebase-database")
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-messaging")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation("com.airbnb.android:lottie-compose:6.6.2")
    implementation ("io.coil-kt:coil-compose:2.4.0")
    implementation(libs.material.v190)
    implementation(libs.gson)
    implementation(libs.androidx.drawerlayout)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.common.ktx)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.navigation.testing)
    testImplementation(libs.junit)
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-manifest:1.5.0")
    androidTestImplementation ("androidx.navigation:navigation-testing:2.6.0")
    androidTestImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("com.airbnb.android:lottie-compose:4.0.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.5.0")
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.48")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation ("org.mockito:mockito-core:4.3.1")
    testImplementation ("org.mockito:mockito-inline:4.3.1")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("org.mockito:mockito-android:4.3.1")
    testImplementation ("org.robolectric:robolectric:4.9")
    testImplementation ("androidx.test:core:1.4.0")
    implementation ("com.itextpdf:itext7-core:7.2.2")
    implementation ("com.google.mlkit:translate:17.0.3")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.testng)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


}