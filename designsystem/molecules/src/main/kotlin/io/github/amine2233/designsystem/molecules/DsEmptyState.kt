package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsIconTile
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Empty cart / no results / error placeholder with optional CTA. */
@Composable
fun DsEmptyState(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: ImageVector? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val c = DsTheme.colors
    Column(modifier.fillMaxWidth().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (icon != null) {
            DsIconTile(
                icon,
                size = 64.dp,
            ) { Icon(icon, contentDescription = null, tint = c.textTertiary, modifier = Modifier.padding(16.dp)) }
            Spacer(Modifier.height(16.dp))
        }
        Text(title, style = DsTheme.typography.title, color = c.textPrimary, textAlign = TextAlign.Center)
        if (description != null) {
            Spacer(Modifier.height(6.dp))
            Text(description, style = DsTheme.typography.body, color = c.textSecondary, textAlign = TextAlign.Center)
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(20.dp))
            DsButton(actionLabel, onClick = onAction, variant = DsButtonVariant.Outlined, size = DsButtonSize.Medium)
        }
    }
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
