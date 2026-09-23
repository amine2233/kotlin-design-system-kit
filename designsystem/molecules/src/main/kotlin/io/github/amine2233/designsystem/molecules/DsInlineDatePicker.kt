package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * The calendar with no dialog around it — for a tablet side pane or a filter panel where the date
 * is the point of the screen and a dialog would be one tap too many.
 *
 * Selection is reported as it happens, so the caller filters or previews live; there is no confirm
 * button because there is nothing to dismiss. Same unit as [DsDatePickerField]: UTC epoch millis.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsInlineDatePicker(
    value: Long?,
    onValueChange: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    initialDisplayedMonthUtcMillis: Long? = value,
    bounds: DsDateBounds = DsDateBounds(),
    title: (@Composable () -> Unit)? = null,
    headline: (@Composable () -> Unit)? = null,
    showModeToggle: Boolean = false,
) {
    val state =
        rememberDatePickerState(
            initialSelectedDateMillis = value,
            initialDisplayedMonthMillis = initialDisplayedMonthUtcMillis,
            selectableDates = bounds.toSelectableDates(),
        )
    LaunchedEffect(state) {
        snapshotFlow { state.selectedDateMillis }.collect { if (it != value) onValueChange(it) }
    }
    DatePicker(
        state = state,
        modifier = modifier.fillMaxWidth(),
        title = title,
        headline = headline,
        showModeToggle = showModeToggle,
        colors = DatePickerDefaults.colors(containerColor = DsTheme.colors.surface),
    )
}

/**
 * The range calendar with no dialog around it — the filter panel of a report screen.
 * Reports only complete periods, so a half-picked range never reaches the caller's query.
 *
 * It scrolls its own list of months, so give it a bounded height; inside a `verticalScroll` column
 * it fails to measure.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsInlineDateRangePicker(
    startUtcMillis: Long?,
    endUtcMillis: Long?,
    onRangeChange: (Long, Long) -> Unit,
    modifier: Modifier = Modifier,
    initialDisplayedMonthUtcMillis: Long? = startUtcMillis,
    bounds: DsDateBounds = DsDateBounds(),
    title: (@Composable () -> Unit)? = null,
    headline: (@Composable () -> Unit)? = null,
    showModeToggle: Boolean = false,
) {
    val state =
        rememberDateRangePickerState(
            initialSelectedStartDateMillis = startUtcMillis,
            initialSelectedEndDateMillis = endUtcMillis,
            initialDisplayedMonthMillis = initialDisplayedMonthUtcMillis,
            selectableDates = bounds.toSelectableDates(),
        )
    LaunchedEffect(state) {
        snapshotFlow { state.selectedStartDateMillis to state.selectedEndDateMillis }.collect { (start, end) ->
            if (start != null && end != null && (start != startUtcMillis || end != endUtcMillis)) onRangeChange(start, end)
        }
    }
    DateRangePicker(
        state = state,
        modifier = modifier.fillMaxWidth(),
        title = title,
        headline = headline,
        showModeToggle = showModeToggle,
        colors = DatePickerDefaults.colors(containerColor = DsTheme.colors.surface),
    )
}

@DsComponentPreview
@Composable
private fun DsInlineDatePickerPreview() =
    DsPreview {
        DsInlineDatePicker(1_772_236_800_000L, {})
    }
