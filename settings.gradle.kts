pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "kotlin-design-system-kit"

include(
    ":designsystem:core",
    ":designsystem:atoms",
    ":designsystem:molecules",
    ":designsystem:organisms",
    ":designsystem:templates",
    ":designsystem:catalog",
    ":sample",
)
