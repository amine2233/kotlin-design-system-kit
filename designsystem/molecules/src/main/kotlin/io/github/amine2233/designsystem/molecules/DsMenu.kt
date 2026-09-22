package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** One action in a [DsMenu]. Destructive entries are tinted with the error color and always come last. */
@Immutable
data class DsMenuItem(
    val label: String,
    val icon: ImageVector? = null,
    val enabled: Boolean = true,
    val destructive: Boolean = false,
    val onClick: () -> Unit,
)

/**
 * Contextual action menu anchored to whatever the caller puts in [anchor] — the "…" of a top bar,
 * a cart line, a product tile. State is the caller's so the anchor can reflect it.
 */
@Composable
fun DsMenu(
    items: List<DsMenuItem>,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    anchor: @Composable () -> Unit,
) {
    val c = DsTheme.colors
    Box(modifier) {
        anchor()
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            containerColor = c.surface,
            shape = DsShapes.md,
        ) {
            items.forEach { item ->
                val tint = if (item.destructive) c.error else c.textPrimary
                DropdownMenuItem(
                    enabled = item.enabled,
                    text = {
                        Text(
                            item.label,
                            style = DsTheme.typography.body,
                            color = if (item.enabled) tint else c.textDisabled,
                        )
                    },
                    leadingIcon =
                        item.icon?.let {
                            {
                                Icon(it, contentDescription = null, tint = if (item.enabled) tint else c.textDisabled)
                            }
                        },
                    onClick = {
                        onDismissRequest()
                        item.onClick()
                    },
                )
            }
        }
    }
}

/** The common case: an icon button that opens a [DsMenu] and owns its own open state. */
@Composable
fun DsOverflowMenu(
    items: List<DsMenuItem>,
    modifier: Modifier = Modifier,
    icon: ImageVector = DsIcons.More,
    contentDescription: String = "Plus d'actions",
    enabled: Boolean = true,
) {
    var open by remember { mutableStateOf(false) }
    DsMenu(items = items, expanded = open, onDismissRequest = { open = false }, modifier = modifier) {
        DsIconButton(
            icon = icon,
            contentDescription = contentDescription,
            onClick = { open = true },
            tint = DsTheme.colors.textSecondary,
            enabled = enabled,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsMenuPreview() =
    DsPreview {
        DsOverflowMenu(
            listOf(
                DsMenuItem("Modifier", DsIcons.Edit) {},
                DsMenuItem("Imprimer le ticket", DsIcons.Print) {},
                DsMenuItem("Mettre en attente", DsIcons.Hold, enabled = false) {},
                DsMenuItem("Supprimer la ligne", DsIcons.Delete, destructive = true) {},
            ),
        )
    }
