package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

enum class DsSnackbarTone { Neutral, Success, Error }

class DsSnackbarVisuals(
    override val message: String,
    val tone: DsSnackbarTone = DsSnackbarTone.Neutral,
    override val actionLabel: String? = null,
    override val withDismissAction: Boolean = false,
    override val duration: SnackbarDuration = if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long,
) : SnackbarVisuals

/** `hostState.showError("Échec du paiement", "Réessayer")` — the design's error snackbar with retry. */
suspend fun SnackbarHostState.showError(
    message: String,
    actionLabel: String? = null,
): SnackbarResult = showSnackbar(DsSnackbarVisuals(message, DsSnackbarTone.Error, actionLabel))

suspend fun SnackbarHostState.showSuccess(message: String): SnackbarResult =
    showSnackbar(DsSnackbarVisuals(message, DsSnackbarTone.Success))

suspend fun SnackbarHostState.showMessage(
    message: String,
    actionLabel: String? = null,
): SnackbarResult = showSnackbar(DsSnackbarVisuals(message, DsSnackbarTone.Neutral, actionLabel))

/** Drop into `Scaffold(snackbarHost = { DsSnackbarHost(hostState) })`. */
@Composable
fun DsSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(hostState, modifier) { data ->
        val tone = (data.visuals as? DsSnackbarVisuals)?.tone ?: DsSnackbarTone.Neutral
        DsSnackbar(
            message = data.visuals.message,
            tone = tone,
            actionLabel = data.visuals.actionLabel,
            onAction = { data.performAction() },
            onDismiss = if (data.visuals.withDismissAction) ({ data.dismiss() }) else null,
        )
    }
}

/** Stateless snackbar surface (also usable in previews / screenshot tests). */
@Composable
fun DsSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    tone: DsSnackbarTone = DsSnackbarTone.Neutral,
    actionLabel: String? = null,
    onAction: () -> Unit = {},
    onDismiss: (() -> Unit)? = null,
) {
    val c = DsTheme.colors
    val (container, content, icon) =
        when (tone) {
            DsSnackbarTone.Neutral -> Triple(c.textPrimary, c.surface, null)
            DsSnackbarTone.Success -> Triple(c.success, c.onPrimary, DsIcons.Success)
            DsSnackbarTone.Error -> Triple(c.error, c.onPrimary, DsIcons.Error)
        }
    Snackbar(
        modifier = modifier.padding(12.dp),
        shape = DsShapes.md,
        containerColor = container,
        contentColor = content,
        action =
            actionLabel?.let {
                { TextButton(onClick = onAction) { Text(it, style = DsTheme.typography.labelStrong, color = content) } }
            },
        dismissAction =
            onDismiss?.let {
                { IconButton(onClick = it) { Icon(DsIcons.Close, contentDescription = "Fermer", tint = content) } }
            },
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            if (icon != null) Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(18.dp))
            Text(message, style = DsTheme.typography.bodyStrong, color = content)
        }
    }
}

@DsComponentPreview
@Composable
private fun DsSnackbarPreview() =
    DsPreview {
        DsSnackbar("Échec du paiement — terminal injoignable", tone = DsSnackbarTone.Error, actionLabel = "Réessayer")
        DsSnackbar("Commande mise en attente", tone = DsSnackbarTone.Success)
        DsSnackbar("Article supprimé", actionLabel = "Annuler", onDismiss = {})
    }

@DsComponentPreview
@Composable
private fun DsSnackbarHostPreview() =
    DsPreview {
        DsSnackbarHost(remember { SnackbarHostState() })
    }
