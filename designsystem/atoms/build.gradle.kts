plugins {
    id("designsystem.compose.library")
}

android.namespace = "io.github.amine2233.designsystem.atoms"

dependencies {
    api(project(":designsystem:core"))
}

dependencies {
    testImplementation(libs.junit)
}
