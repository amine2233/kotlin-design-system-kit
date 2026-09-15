plugins {
    id("designsystem.compose.library")
    alias(libs.plugins.android.screenshot)
}

android {
    namespace = "io.github.amine2233.designsystem.catalog"
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
}

// References are recorded on macOS and validated on Linux CI: allow sub-pixel anti-aliasing
// differences (0.1 % of pixels). A real regression (color, spacing, missing element) is far above this.
// AGP 9's test-suite DSL no longer exposes the threshold, so it is set on the validation task.
tasks.withType<com.android.compose.screenshot.tasks.PreviewScreenshotValidationTask>().configureEach {
    testEngineInput.threshold.set(0.001f)
}

dependencies {
    api(project(":designsystem:templates"))
    screenshotTestImplementation(libs.androidx.compose.ui.tooling)
    screenshotTestImplementation(libs.android.screenshot.validation.api)
}
