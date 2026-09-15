plugins {
    id("designsystem.compose.library")
    alias(libs.plugins.android.screenshot)
}

android {
    namespace = "io.github.amine2233.designsystem.catalog"
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

dependencies {
    api(project(":designsystem:templates"))
    screenshotTestImplementation(libs.androidx.compose.ui.tooling)
    screenshotTestImplementation(libs.android.screenshot.validation.api)
}
