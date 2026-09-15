// AGP + Compose compiler plugins are on the buildSrc classpath (see buildSrc/build.gradle.kts);
// modules apply them by id without a version.
plugins {
    alias(libs.plugins.android.screenshot) apply false
}

allprojects {
    group = "io.github.amine2233"
    version = (findProperty("version") as String?)?.takeIf { it.isNotBlank() } ?: "0.1.0-SNAPSHOT"
}
