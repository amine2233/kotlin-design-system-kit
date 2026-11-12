package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.amine2233.designsystem.atoms.DsIconTile
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme

/** Empty cart / no results placeholder with optional CTA. For content that failed to load, use [DsFailureState]. */
@Composable
fun DsEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    DsStateLayout(
        title,
        modifier = modifier,
        description = description,
        icon =
            if (icon != null) {
                {
                    DsIconTile(icon, size = DsSpacing.xxxl) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = DsTheme.colors.textTertiary,
                            modifier = Modifier.padding(DsSpacing.md),
                        )
                    }
                }
            } else {
                null
            },
        actionLabel = actionLabel,
        onAction = onAction,
    )
}

@DsComponentPreview
@Composable
private fun DsEmptyStatePreview() =
    DsPreview {
        DsEmptyState(
            "Panier vide",
            description = "Touchez un produit pour l'ajouter.",
            icon = DsIcons.Cart,
            actionLabel = "Voir le catalogue",
            onAction = {},
        )
    }
