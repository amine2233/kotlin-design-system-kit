package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsSpacing
import io.github.amine2233.designsystem.core.DsTheme

/** Placeholder for content that failed to load, with an optional retry. The error counterpart of [DsEmptyState]. */
@Composable
fun DsFailureState(
    title: String,
    modifier: Modifier = Modifier,
    message: String? = null,
    icon: ImageVector? = DsIcons.Error,
    retryLabel: String? = null,
    onRetry: (() -> Unit)? = null,
) {
    DsStateLayout(
        title,
        modifier = modifier,
        description = message,
        icon =
            if (icon != null) {
                { DsFailureIconTile(icon) }
            } else {
                null
            },
        actionLabel = retryLabel,
        onAction = onRetry,
    )
}

@Composable
private fun DsFailureIconTile(icon: ImageVector) {
    Box(
        Modifier.size(DsSpacing.xxxl).background(DsTheme.colors.errorContainer, DsShapes.md),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = DsTheme.colors.error,
            modifier = Modifier.padding(DsSpacing.md),
        )
    }
}

@DsComponentPreview
@Composable
private fun DsFailureStatePreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsSpacing.md)) {
            DsFailureState(
                "Chargement impossible",
                message = "Vérifiez la connexion puis réessayez.",
                retryLabel = "Réessayer",
                onRetry = {},
            )
            DsFailureState("Chargement impossible", retryLabel = "Réessayer", onRetry = {})
            DsFailureState(
                "Chargement impossible",
                message = "Vérifiez la connexion puis réessayez.",
                icon = null,
                retryLabel = "Réessayer",
                onRetry = {},
            )
            DsFailureState("Chargement impossible", message = "Vérifiez la connexion puis réessayez.")
        }
    }
