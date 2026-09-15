plugins {
    id("designsystem.compose.library")
}

android.namespace = "io.github.amine2233.designsystem.organisms"

dependencies {
    api(project(":designsystem:molecules"))
}

dependencies {
    testImplementation(libs.junit)
}
