package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsInlineDatePicker
import io.github.amine2233.designsystem.molecules.DsInlineDateRangePicker
import io.github.amine2233.designsystem.molecules.dsFormatDate
import io.github.amine2233.designsystem.molecules.dsFormatDateRange

private const val FEB28 = 1_772_236_800_000L
private const val MAR06 = 1_772_755_200_000L

/**
 * The calendars with no dialog around them. This page is also the only snapshot coverage the
 * calendar can get: inside DsDatePickerField it lives in a dialog, and a popup never renders.
 */
@Composable
fun CalendarGallery(modifier: Modifier = Modifier) {
    // No verticalScroll here: the range picker scrolls its own month list and needs a bounded height.
    Column(modifier.fillMaxWidth()) {
        CatalogSection("Inline date picker") {
            var day by remember { mutableStateOf<Long?>(FEB28) }
            DsInlineDatePicker(day, { day = it })
            DsText(
                day?.let(::dsFormatDate) ?: "Aucune date",
                style = DsTheme.typography.bodyStrong,
                color = DsTheme.colors.primary,
            )
        }
        CatalogSection("Inline range picker") {
            var range by remember { mutableStateOf(FEB28 to MAR06) }
            DsInlineDateRangePicker(
                range.first,
                range.second,
                { start, end -> range = start to end },
                modifier = Modifier.height(420.dp),
            )
            DsText(
                dsFormatDateRange(range.first, range.second).orEmpty(),
                style = DsTheme.typography.bodyStrong,
                color = DsTheme.colors.primary,
            )
        }
    }
}

@Preview(name = "calendars", device = "spec:width=420dp,height=900dp,dpi=320", showBackground = true)
@Composable
private fun CalendarGalleryPreview() = DsTheme { CalendarGallery() }
