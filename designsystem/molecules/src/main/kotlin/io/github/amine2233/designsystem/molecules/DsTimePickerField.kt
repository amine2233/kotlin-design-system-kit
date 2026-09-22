package io.github.amine2233.designsystem.molecules

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsPickerField
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Minutes since midnight → "HH:mm". */
fun dsFormatTime(minutesOfDay: Int): String = "%02d:%02d".format(minutesOfDay / 60, minutesOfDay % 60)

/**
 * Time entry: a read-only field that opens the Material clock dialog.
 *
 * The value is minutes since midnight — small, comparable and timezone-free; the caller converts
 * to whatever time type it uses. Pair it with [DsDatePickerField] for a full timestamp.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsTimePickerField(
    value: Int?,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Sélectionner une heure",
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    is24Hour: Boolean = true,
    confirmText: String = "Valider",
    dismissText: String = "Annuler",
    format: (Int) -> String = ::dsFormatTime,
) {
    var open by remember { mutableStateOf(false) }
    DsPickerField(
        valueText = value?.let(format),
        placeholder = placeholder,
        trailingIcon = DsIcons.Clock,
        onClick = { open = true },
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        isError = isError,
        required = required,
        enabled = enabled,
    )
    if (open) {
        val state =
            rememberTimePickerState(
                initialHour = (value ?: 0) / 60,
                initialMinute = (value ?: 0) % 60,
                is24Hour = is24Hour,
            )
        AlertDialog(
            onDismissRequest = { open = false },
            shape = DsShapes.lg,
            containerColor = DsTheme.colors.surface,
            text = {
                TimePicker(
                    state = state,
                    colors =
                        TimePickerDefaults.colors(
                            selectorColor = DsTheme.colors.primary,
                            periodSelectorSelectedContainerColor = DsTheme.colors.primaryContainer,
                            timeSelectorSelectedContainerColor = DsTheme.colors.primaryContainer,
                        ),
                )
            },
            confirmButton = {
                DsButton(confirmText, size = DsButtonSize.Small, onClick = {
                    open = false
                    onValueChange(state.hour * 60 + state.minute)
                })
            },
            dismissButton = {
                DsButton(dismissText, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small, onClick = { open = false })
            },
        )
    }
}

@DsComponentPreview
@Composable
private fun DsTimePickerFieldPreview() =
    DsPreview {
        DsTimePickerField(8 * 60 + 30, {}, label = "Ouverture")
        DsTimePickerField(null, {}, label = "Fermeture", isError = true, supportingText = "Obligatoire")
    }
