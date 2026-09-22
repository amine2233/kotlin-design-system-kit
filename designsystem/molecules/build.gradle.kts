plugins {
    id("designsystem.compose.library")
}

android.namespace = "io.github.amine2233.designsystem.molecules"

dependencies {
    api(project(":designsystem:atoms"))
    // The photo picker launcher (rememberDsImagePicker) needs the activity result APIs.
    api(libs.androidx.activity.compose)
}

dependencies {
    testImplementation(libs.junit)
}
