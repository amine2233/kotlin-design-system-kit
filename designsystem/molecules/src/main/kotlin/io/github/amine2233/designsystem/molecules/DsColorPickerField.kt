package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsPickerField
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPalette
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Hex label for a swatch, e.g. "#00796B". */
fun dsFormatColor(color: Color): String = "#%06X".format(color.toArgb() and 0xFFFFFF)

/**
 * Swatch grid — the picker itself, without a field around it.
 *
 * A fixed palette rather than a free HSV wheel: category and tag colors must stay legible against
 * the surface and readable with white text, which an arbitrary color cannot promise.
 */
@Composable
fun DsColorGrid(
    selected: Color?,
    onSelect: (Color) -> Unit,
    modifier: Modifier = Modifier,
    colors: List<Color> = DsPalette.swatches,
    perRow: Int = 5,
    swatchSize: androidx.compose.ui.unit.Dp = 44.dp,
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
        colors.chunked(perRow).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                row.forEach { color ->
                    val isSelected = selected == color
                    Box(
                        modifier =
                            Modifier
                                .size(swatchSize)
                                .clip(DsShapes.sm)
                                .background(color)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) DsTheme.colors.textPrimary else DsTheme.colors.border,
                                    shape = DsShapes.sm,
                                ).selectable(selected = isSelected, role = Role.RadioButton) { onSelect(color) },
                        contentAlignment = Alignment.Center,
                    ) {
                        if (isSelected) {
                            Icon(
                                DsIcons.Check,
                                contentDescription = null,
                                tint = if (color.luminance() > 0.5f) DsPalette.Grey900 else DsPalette.Grey0,
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Color entry: the swatch and its hex in a field, the grid in a dialog. */
@Composable
fun DsColorPickerField(
    value: Color?,
    onValueChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Choisir une couleur",
    colors: List<Color> = DsPalette.swatches,
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    dialogTitle: String = "Couleur",
    dismissText: String = "Annuler",
    format: (Color) -> String = ::dsFormatColor,
) {
    var open by remember { mutableStateOf(false) }
    DsPickerField(
        valueText = value?.let(format),
        placeholder = placeholder,
        trailingIcon = DsIcons.Palette,
        onClick = { open = true },
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        isError = isError,
        required = required,
        enabled = enabled,
        leadingContent =
            value?.let {
                {
                    Box(
                        Modifier
                            .size(24.dp)
                            .clip(DsShapes.xs)
                            .background(it)
                            .border(1.dp, DsTheme.colors.border, DsShapes.xs),
                    )
                }
            },
    )
    if (open) {
        AlertDialog(
            onDismissRequest = { open = false },
            shape = DsShapes.lg,
            containerColor = DsTheme.colors.surface,
            title = { DsText(dialogTitle, style = DsTheme.typography.title) },
            text = {
                DsColorGrid(selected = value, colors = colors, onSelect = {
                    open = false
                    onValueChange(it)
                })
            },
            confirmButton = {
                DsButton(dismissText, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small, onClick = { open = false })
            },
        )
    }
}

@DsComponentPreview
@Composable
private fun DsColorPickerFieldPreview() =
    DsPreview {
        DsColorPickerField(DsPalette.Teal700, {}, label = "Couleur de la catégorie", required = true)
        DsColorPickerField(null, {}, label = "Couleur du tag")
        DsColorGrid(selected = DsPalette.Orange700, onSelect = {})
    }
