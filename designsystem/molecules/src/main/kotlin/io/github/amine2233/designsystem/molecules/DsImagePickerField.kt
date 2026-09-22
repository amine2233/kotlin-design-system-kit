package io.github.amine2233.designsystem.molecules

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.atoms.DsImageSlot
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsImages
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Opens the Android photo picker and hands back the chosen [Uri] (null when cancelled).
 *
 * The system picker needs no storage permission and shows only what the user selects, which is why
 * it is preferred over a file-manager intent. Returns the launch lambda to pass to
 * [DsImagePickerField]'s `onPick`.
 */
@Composable
fun rememberDsImagePicker(
    mediaType: PickVisualMedia.VisualMediaType = PickVisualMedia.ImageOnly,
    onPicked: (Uri?) -> Unit,
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(PickVisualMedia(), onPicked)
    return remember(launcher, mediaType) { { launcher.launch(PickVisualMediaRequest(mediaType)) } }
}

/**
 * Image entry: thumbnail, pick action, and a remove button once something is chosen.
 *
 * It takes a [Painter], not a Uri or a bitmap: the design system stays image-loader agnostic, so
 * the app decodes with whatever it already uses (Coil, a BitmapPainter) and the field only lays out.
 */
@Composable
fun DsImagePickerField(
    painter: Painter?,
    onPick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    pickText: String = "Choisir une image",
    replaceText: String = "Remplacer l'image",
    onRemove: (() -> Unit)? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    previewSize: Dp = 72.dp,
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
            DsImageSlot(
                painter = painter,
                contentDescription = label,
                modifier =
                    Modifier
                        .size(previewSize)
                        .border(1.dp, if (isError) c.error else c.border, DsShapes.md)
                        .clickable(enabled = enabled, onClick = onPick),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xxs)) {
                DsText(
                    if (painter == null) pickText else replaceText,
                    style = DsTheme.typography.bodyStrong,
                    color = if (enabled) c.primary else c.textDisabled,
                    modifier = Modifier.clickable(enabled = enabled, onClick = onPick),
                )
                DsText("JPG ou PNG", style = DsTheme.typography.caption, color = c.textTertiary)
            }
            if (painter != null && onRemove != null) {
                DsIconButton(DsIcons.Delete, contentDescription = "Retirer l'image", onClick = onRemove, tint = c.error, enabled = enabled)
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsImagePickerFieldPreview() =
    DsPreview {
        DsImagePickerField(painterResource(DsImages.ProductPlaceholder), {}, label = "Photo de l'article", onRemove = {})
        DsImagePickerField(null, {}, label = "Logo du commerce", isError = true, supportingText = "Obligatoire")
    }
