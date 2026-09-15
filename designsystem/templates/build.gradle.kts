plugins {
    id("designsystem.compose.library")
}

android.namespace = "io.github.amine2233.designsystem.templates"

dependencies {
    api(project(":designsystem:organisms"))
}
