package io.github.amine2233.designsystem.organisms

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Confirmation dialog ("Vider le panier ?"). [destructive] renders the confirm CTA in the error color. */
@Composable
fun DsDialog(
    title: String,
    text: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    dismissLabel: String = "Annuler",
    destructive: Boolean = false,
) {
    val c = DsTheme.colors
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = DsShapes.xl,
        containerColor = c.surface,
        titleContentColor = c.textPrimary,
        textContentColor = c.textSecondary,
        title = { Text(title, style = DsTheme.typography.titleLarge) },
        text = { Text(text, style = DsTheme.typography.body) },
        confirmButton = {
            DsButton(
                confirmLabel,
                onClick = onConfirm,
                size = DsButtonSize.Medium,
                variant = if (destructive) DsButtonVariant.Danger else DsButtonVariant.Primary,
            )
        },
        dismissButton = { DsButton(dismissLabel, onClick = onDismiss, size = DsButtonSize.Medium, variant = DsButtonVariant.Ghost) },
    )
}

@Preview(name = "dialog")
@Composable
private fun DsDialogPreview() =
    DsTheme {
        DsDialog("Vider le panier ?", "Les 5 articles seront supprimés.", confirmLabel = "Vider", onConfirm = {
        }, onDismiss = {}, destructive = true)
    }
