package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

enum class DsBannerTone { Info, Success, Warning, Error }

/** Inline alert (tinted container + border). For transient messages use DsSnackbar instead. */
@Composable
fun DsBanner(
    message: String,
    modifier: Modifier = Modifier,
    tone: DsBannerTone = DsBannerTone.Info,
    title: String? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
) {
    val c = DsTheme.colors
    val (bg, fg, icon) = when (tone) {
        DsBannerTone.Info -> Triple(c.infoContainer, c.info, Icons.Default.Info)
        DsBannerTone.Success -> Triple(c.successContainer, c.success, Icons.Default.CheckCircle)
        DsBannerTone.Warning -> Triple(c.warningContainer, c.warning, Icons.Default.Warning)
        DsBannerTone.Error -> Triple(c.errorContainer, c.error, Icons.Default.Warning)
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(bg, DsShapes.md)
            .border(1.dp, fg.copy(alpha = 0.35f), DsShapes.md)
            .padding(start = 12.dp, end = 4.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(icon, contentDescription = null, tint = fg, modifier = Modifier.size(20.dp))
        Column(Modifier.weight(1f)) {
            if (title != null) Text(title, style = DsTheme.typography.bodyStrong, color = fg)
            Text(message, style = DsTheme.typography.body, color = c.textPrimary)
        }
        if (actionLabel != null && onAction != null) {
            DsButton(actionLabel, onClick = onAction, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small)
        }
        if (onDismiss != null) DsIconButton(Icons.Default.Close, contentDescription = "Fermer", onClick = onDismiss, tint = c.textSecondary)
    }
}

@DsComponentPreview
@Composable
private fun DsBannerPreview() = DsPreview {
    DsBanner("Le terminal est déconnecté.", tone = DsBannerTone.Error, title = "Paiement carte indisponible", actionLabel = "Réessayer", onAction = {})
    Spacer(Modifier.height(6.dp))
    DsBanner("Remise appliquée.", tone = DsBannerTone.Success, onDismiss = {})
    Spacer(Modifier.height(6.dp))
    DsBanner("Pourboire calculé sur le TTC.", tone = DsBannerTone.Info)
    Spacer(Modifier.height(6.dp))
    DsBanner("Stock faible : Croissant (3)", tone = DsBannerTone.Warning)
}
