// Standalone prototype build (Senior decision Q17).
// Intentionally NOT included in any root build. Prototype code only, never production.
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    // Default mode (PREFER_PROJECT) on purpose: the Kotlin Gradle plugin registers its own
    // Node.js / Yarn distribution repositories on the root project for the wasmJs target.
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "prototype-01-skeleton"

include(":screens")
include(":androidApp")
