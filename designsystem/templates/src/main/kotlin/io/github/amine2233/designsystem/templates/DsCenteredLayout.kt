package io.github.amine2233.designsystem.templates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Onboarding / loading: content capped at [maxWidth] and centered — same composable on phone and tablet. */
@Composable
fun DsCenteredLayout(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 520.dp,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(Modifier.widthIn(max = maxWidth), content = content)
    }
}

@DsScreenPreview
@Composable
private fun DsCenteredLayoutPreview() = DsTheme {
    DsCenteredLayout { DsText("Centré, largeur max 520dp", style = DsTheme.typography.headline) }
}
