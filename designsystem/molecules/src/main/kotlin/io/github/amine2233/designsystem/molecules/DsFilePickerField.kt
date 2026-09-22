package io.github.amine2233.designsystem.molecules

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.atoms.DsIconTile
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Opens the system document picker and hands back the chosen [Uri] (null when cancelled).
 *
 * [OpenDocument] rather than a get-content intent: it returns a URI the app can take persistable
 * permission on, which is what an attachment kept across restarts needs. [mimeTypes] filters what
 * the picker offers — narrow it, a picker showing every file invites the wrong one.
 */
@Composable
fun rememberDsFilePicker(
    mimeTypes: Array<String> = arrayOf("application/pdf"),
    onPicked: (Uri?) -> Unit,
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(OpenDocument(), onPicked)
    return remember(launcher, mimeTypes) { { launcher.launch(mimeTypes) } }
}

/**
 * File entry: the picked document's name and caption, or the invitation to choose one.
 *
 * Name and caption are given by the caller — resolving a display name and a size from a content
 * URI is a query against the content resolver, which is the app's job, not the design system's.
 */
@Composable
fun DsFilePickerField(
    fileName: String?,
    onPick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    pickText: String = "Choisir un fichier",
    caption: String? = null,
    icon: ImageVector = DsIcons.Receipt,
    onRemove: (() -> Unit)? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
) {
    val c = DsTheme.colors
    DsFormField(
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        isError = isError,
        required = required,
        enabled = enabled,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.smd),
        ) {
            DsIconTile(icon, selected = fileName != null)
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xxs)) {
                DsText(
                    fileName ?: pickText,
                    style = DsTheme.typography.bodyStrong,
                    color =
                        if (!enabled) {
                            c.textDisabled
                        } else if (fileName == null) {
                            c.primary
                        } else {
                            c.textPrimary
                        },
                    maxLines = 1,
                    modifier = if (enabled && fileName == null) Modifier.clickable(onClick = onPick) else Modifier,
                )
                if (caption != null) DsText(caption, style = DsTheme.typography.caption, color = c.textTertiary)
            }
            if (fileName == null) {
                DsIconButton(DsIcons.Add, contentDescription = pickText, onClick = onPick, tint = c.primary, enabled = enabled)
            } else {
                DsIconButton(DsIcons.Edit, contentDescription = "Remplacer le fichier", onClick = onPick, enabled = enabled)
                if (onRemove != null) {
                    DsIconButton(DsIcons.Delete, contentDescription = "Retirer le fichier", onClick = onRemove, tint = c.error)
                }
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsFilePickerFieldPreview() =
    DsPreview {
        DsFilePickerField("facture-2026-03.pdf", {}, label = "Justificatif", caption = "PDF · 240 Ko", onRemove = {})
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsFilePickerField(null, {}, label = "Bon de commande", isError = true, supportingText = "Obligatoire")
    }
