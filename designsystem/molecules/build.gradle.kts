plugins {
    id("designsystem.compose.library")
}

android.namespace = "io.github.amine2233.designsystem.molecules"

dependencies {
    api(project(":designsystem:atoms"))
}

dependencies {
    testImplementation(libs.junit)
}
