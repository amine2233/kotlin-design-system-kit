package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Read-only field that opens something else — a date dialog, a color grid, the photo picker.
 *
 * It renders the closed state only: [DsFieldBox] inside a [DsFormField]. The component that owns
 * the picker passes [onClick] and formats [valueText], so every picker in the system has the same
 * box, height and error treatment.
 */
@Composable
fun DsPickerField(
    valueText: String?,
    placeholder: String,
    trailingIcon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    leadingContent: (@Composable () -> Unit)? = null,
) {
    DsFormField(
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        isError = isError,
        required = required,
        enabled = enabled,
    ) {
        DsFieldBox(
            valueText = valueText,
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            onClick = onClick,
            isError = isError,
            enabled = enabled,
            leadingContent = leadingContent,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsPickerFieldPreview() =
    DsPreview {
        DsPickerField("12/03/2026", "Sélectionner une date", DsIcons.Calendar, {}, label = "Date de clôture")
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsPickerField(null, "Sélectionner une heure", DsIcons.Clock, {}, label = "Heure", isError = true, supportingText = "Obligatoire")
    }
