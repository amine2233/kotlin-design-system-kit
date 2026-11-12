package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme

/** Centred icon tile, heading title, description and outlined CTA shared by [DsEmptyState] and [DsFailureState]. */
@Composable
internal fun DsStateLayout(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    icon: (@Composable () -> Unit)? = null,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    val c = DsTheme.colors
    Column(modifier.fillMaxWidth().padding(DsSpacing.xl), horizontalAlignment = Alignment.CenterHorizontally) {
        if (icon != null) {
            icon()
            Spacer(Modifier.height(DsSpacing.md))
        }
        Text(
            title,
            modifier = Modifier.semantics { heading() },
            style = DsTheme.typography.title,
            color = c.textPrimary,
            textAlign = TextAlign.Center,
        )
        if (description != null) {
            Spacer(Modifier.height(6.dp))
            Text(description, style = DsTheme.typography.body, color = c.textSecondary, textAlign = TextAlign.Center)
        }
        if (actionLabel != null && onAction != null) {
            Spacer(Modifier.height(DsSpacing.mlg))
            DsButton(actionLabel, onClick = onAction, variant = DsButtonVariant.Outlined, size = DsButtonSize.Medium)
        }
    }
}
