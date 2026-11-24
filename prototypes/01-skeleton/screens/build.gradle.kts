import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
}

kotlin {
    androidLibrary {
        namespace = "com.expensesshared.prototypes.skeleton.screens"
        compileSdk = 36
        minSdk = 29
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("skeleton")
        browser {
            commonWebpackConfig {
                outputFileName = "skeleton.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
        }
    }
}

dependencies {
    // Lets Android Studio render the common @Preview functions for the Android target.
    add("androidRuntimeClasspath", compose.uiTooling)
}
