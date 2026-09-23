package io.github.amine2233.designsystem.molecules

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
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
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val IsoDayFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

/** Day-precision UTC millis → "dd/MM/yyyy". Pass your own [DsDatePickerField.format] for a localised label. */
fun dsFormatDate(utcMillis: Long): String = IsoDayFormatter.format(Instant.ofEpochMilli(utcMillis).atZone(ZoneOffset.UTC))

/**
 * Date entry: a read-only field that opens the Material date dialog.
 *
 * The value is UTC epoch millis at day precision — the same unit the dialog returns — so no
 * date type crosses the design system boundary. [format] stays with the caller because the
 * display format is a locale decision, not a design one.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsDatePickerField(
    value: Long?,
    onValueChange: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Sélectionner une date",
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    bounds: DsDateBounds = DsDateBounds(),
    confirmText: String = "Valider",
    dismissText: String = "Annuler",
    format: (Long) -> String = ::dsFormatDate,
) {
    var open by remember { mutableStateOf(false) }
    DsPickerField(
        valueText = value?.let(format),
        placeholder = placeholder,
        trailingIcon = DsIcons.Calendar,
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
            rememberDatePickerState(
                initialSelectedDateMillis = value,
                selectableDates = bounds.toSelectableDates(),
            )
        DatePickerDialog(
            onDismissRequest = { open = false },
            shape = DsShapes.lg,
            colors = DatePickerDefaults.colors(containerColor = DsTheme.colors.surface),
            confirmButton = {
                DsButton(confirmText, size = DsButtonSize.Small, onClick = {
                    open = false
                    onValueChange(state.selectedDateMillis)
                })
            },
            dismissButton = {
                DsButton(dismissText, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small, onClick = { open = false })
            },
        ) {
            DatePicker(state = state, colors = DatePickerDefaults.colors(containerColor = DsTheme.colors.surface))
        }
    }
}

@DsComponentPreview
@Composable
private fun DsDatePickerFieldPreview() =
    DsPreview {
        DsDatePickerField(1_772_236_800_000L, {}, label = "Date de clôture", required = true)
        DsDatePickerField(null, {}, label = "Date de livraison", supportingText = "Facultatif")
    }
