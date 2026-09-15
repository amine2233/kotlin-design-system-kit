package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Rounded square icon container (payment method, product thumbnail placeholder). Teal-tinted when [selected]. */
@Composable
fun DsIconTile(
    icon: ImageVector?,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    selected: Boolean = false,
    content: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    Box(
        modifier =
            modifier
                .size(size)
                .background(if (selected) c.primaryContainerBorder else c.surfaceSubtle, DsShapes.md)
                .border(1.dp, if (selected) c.primaryContainerBorder else c.surfaceMuted, DsShapes.md),
        contentAlignment = Alignment.Center,
    ) {
        when {
            content != null -> {
                content()
            }

            icon != null -> {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = if (selected) c.primary else c.textSecondary,
                    modifier =
                        Modifier.size(size / 2),
                )
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsIconTilePreview() =
    DsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DsIconTile(DsIcons.Card)
            DsIconTile(DsIcons.Cash, selected = true)
            DsIconTile(DsIcons.Coffee, size = 36.dp)
        }
    }
