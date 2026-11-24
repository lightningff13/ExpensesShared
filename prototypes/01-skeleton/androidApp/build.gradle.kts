plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

android {
    namespace = "com.expensesshared.prototypes.skeleton.android"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.expensesshared.prototypes.skeleton"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "0.0.1-prototype"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":screens"))
    implementation(libs.androidx.activity.compose)
    implementation(compose.runtime)
    implementation(compose.material3)
}
