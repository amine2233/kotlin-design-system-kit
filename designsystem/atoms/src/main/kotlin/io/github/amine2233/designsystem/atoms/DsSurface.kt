package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsElevation
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Bordered white card, 12dp radius, optional selection state (teal border + tint). */
@Composable
fun DsCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    selected: Boolean = false,
    shape: Shape = DsShapes.card,
    containerColor: Color = if (selected) DsTheme.colors.primaryContainer else DsTheme.colors.surface,
    borderColor: Color = if (selected) DsTheme.colors.primary else DsTheme.colors.border,
    borderWidth: Dp = if (selected) 2.dp else 1.dp,
    elevation: Dp = DsElevation.none,
    contentPadding: Dp = 0.dp,
    content: @Composable () -> Unit,
) {
    val border = BorderStroke(borderWidth, borderColor)
    val inner: @Composable () -> Unit = { Box(Modifier.padding(contentPadding)) { content() } }
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = containerColor,
            border = border,
            shadowElevation = elevation,
            content = inner,
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            border = border,
            shadowElevation = elevation,
            content = inner,
        )
    }
}
