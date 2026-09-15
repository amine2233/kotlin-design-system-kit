package io.github.amine2233.designsystem.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp

/** Wrap component previews: theme + surface background + padding. */
@Composable
fun DsPreview(content: @Composable () -> Unit) {
    DsTheme {
        Column(Modifier.background(DsTheme.colors.background).padding(16.dp)) { content() }
    }
}

/** Light + dark at phone width — the default annotation for component previews. */
@PreviewLightDark
@Preview(name = "phone", widthDp = 375)
annotation class DsComponentPreview

@Preview(name = "phone", device = "spec:width=375dp,height=780dp,dpi=420")
@Preview(name = "tablet", device = "spec:width=960dp,height=600dp,dpi=320")
annotation class DsScreenPreview
