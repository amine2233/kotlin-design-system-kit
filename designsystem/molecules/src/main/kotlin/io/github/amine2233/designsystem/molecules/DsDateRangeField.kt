package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.height
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.atoms.DsPickerField
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Label for a range: "28/02/2026 → 05/03/2026", or the open end as [openEnd] while only the
 * start is chosen. Null when nothing is selected, so the field shows its placeholder.
 */
fun dsFormatDateRange(
    startUtcMillis: Long?,
    endUtcMillis: Long?,
    format: (Long) -> String = ::dsFormatDate,
    separator: String = " → ",
    openEnd: String = "…",
): String? =
    when {
        startUtcMillis == null && endUtcMillis == null -> null
        startUtcMillis == null -> format(endUtcMillis!!)
        endUtcMillis == null -> format(startUtcMillis) + separator + openEnd
        else -> format(startUtcMillis) + separator + format(endUtcMillis)
    }

/**
 * Period entry: one field holding both ends, opening the Material range calendar.
 *
 * Two [DsDatePickerField]s would let a user pick an end before the start and leave the screen to
 * catch it; the range calendar cannot produce an inverted period, and this field only reports a
 * change once both ends exist. Values are UTC epoch millis at day precision, like the single-date
 * field.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsDateRangeField(
    startUtcMillis: Long?,
    endUtcMillis: Long?,
    onRangeChange: (Long?, Long?) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "Sélectionner une période",
    supportingText: String? = null,
    isError: Boolean = false,
    required: Boolean = false,
    enabled: Boolean = true,
    confirmText: String = "Valider",
    dismissText: String = "Annuler",
    format: (Long) -> String = ::dsFormatDate,
) {
    var open by remember { mutableStateOf(false) }
    DsPickerField(
        valueText = dsFormatDateRange(startUtcMillis, endUtcMillis, format),
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
            rememberDateRangePickerState(
                initialSelectedStartDateMillis = startUtcMillis,
                initialSelectedEndDateMillis = endUtcMillis,
            )
        DatePickerDialog(
            onDismissRequest = { open = false },
            shape = DsShapes.lg,
            colors = DatePickerDefaults.colors(containerColor = DsTheme.colors.surface),
            confirmButton = {
                DsButton(
                    confirmText,
                    size = DsButtonSize.Small,
                    enabled = state.selectedStartDateMillis != null && state.selectedEndDateMillis != null,
                    onClick = {
                        open = false
                        onRangeChange(state.selectedStartDateMillis, state.selectedEndDateMillis)
                    },
                )
            },
            dismissButton = {
                DsButton(dismissText, variant = DsButtonVariant.Ghost, size = DsButtonSize.Small, onClick = { open = false })
            },
        ) {
            DateRangePicker(
                state = state,
                modifier = Modifier.height(520.dp),
                colors = DatePickerDefaults.colors(containerColor = DsTheme.colors.surface),
            )
        }
    }
}

@DsComponentPreview
@Composable
private fun DsDateRangeFieldPreview() =
    DsPreview {
        DsDateRangeField(1_772_236_800_000L, 1_772_755_200_000L, { _, _ -> }, label = "Période du rapport")
        DsDateRangeField(1_772_236_800_000L, null, { _, _ -> }, label = "Début choisi")
        DsDateRangeField(null, null, { _, _ -> }, label = "Période", isError = true, supportingText = "Obligatoire")
    }
