plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("maven-publish")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    compileSdk = libs.findVersion("android-compileSdk").get().requiredVersion.toInt()
    defaultConfig.minSdk = libs.findVersion("android-minSdk").get().requiredVersion.toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures.compose = true
    publishing {
        singleVariant("release") { withSourcesJar() }
    }
}

// Consumable from another project: io.github.amine2233:designsystem-<layer>:<version>
group = "io.github.amine2233"
version = libs.findVersion("designsystem").get().requiredVersion

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                artifactId = "designsystem-${project.name}"
            }
        }
    }
}

dependencies {
    fun lib(alias: String) = libs.findLibrary(alias).get()
    "api"(platform(lib("androidx-compose-bom")))
    "api"(lib("androidx-compose-foundation"))
    "api"(lib("androidx-compose-material3"))
    "api"(lib("androidx-compose-material-icons-core"))
    "api"(lib("androidx-compose-material-icons"))
    "api"(lib("androidx-compose-ui"))
    "api"(lib("androidx-compose-ui-tooling-preview"))
    "debugImplementation"(lib("androidx-compose-ui-tooling"))
}
