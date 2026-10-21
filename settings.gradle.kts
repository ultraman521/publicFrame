pluginManagement {
    repositories {
        // Explicit mirror of google(): the maven.google.com alias is unreachable
        // on some networks, while this canonical URL serves the same artifacts.
        maven(url = "https://dl.google.com/android/maven2") {
            name = "GoogleAndroid"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = "https://dl.google.com/android/maven2") {
            name = "GoogleAndroid"
        }
        mavenCentral()
    }
}

rootProject.name = "PublicFrame"

include(":app")
include(":framework")
