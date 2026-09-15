plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.compose")
    id("maven-publish")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    compileSdk =
        libs
            .findVersion("android-compileSdk")
            .get()
            .requiredVersion
            .toInt()
    defaultConfig.minSdk =
        libs
            .findVersion("android-minSdk")
            .get()
            .requiredVersion
            .toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures.compose = true
    publishing {
        singleVariant("release") { withSourcesJar() }
    }
}

// Published to GitHub Packages by `mise run publish --version X.Y.Z` (semantic-release).
// group/version come from the root allprojects block (-Pversion).
afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("gpr") {
                from(components["release"])
                groupId = project.group.toString()
                artifactId = "designsystem-${project.name}"
                version = project.version.toString()
                pom {
                    name.set("Design System Kit — ${project.name}")
                    description.set("Jetpack Compose design system (${project.name} layer) for Caisse Pro apps")
                    url.set("https://github.com/amine2233/kotlin-design-system-kit")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("amine2233")
                            name.set("Amine Bensalah")
                        }
                    }
                    scm { url.set("https://github.com/amine2233/kotlin-design-system-kit") }
                }
            }
        }
        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/amine2233/kotlin-design-system-kit")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
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
