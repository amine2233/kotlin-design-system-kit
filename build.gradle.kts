// AGP + Compose compiler plugins are on the buildSrc classpath (see buildSrc/build.gradle.kts);
// modules apply them by id without a version.
plugins {
    alias(libs.plugins.android.screenshot) apply false
}
