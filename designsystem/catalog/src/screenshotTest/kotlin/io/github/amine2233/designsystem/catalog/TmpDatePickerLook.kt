package io.github.amine2233.designsystem.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.tools.screenshot.PreviewTest
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsButtonVariant
import io.github.amine2233.designsystem.core.DsTheme

private const val LOOK = "spec:width=420dp,height=1500dp,dpi=320"
private const val FEB28 = 1_772_236_800_000L
private const val MAR06 = 1_772_755_200_000L

@OptIn(ExperimentalMaterial3Api::class)
@PreviewTest
@Preview(name = "tmp_date_look", device = LOOK, showBackground = true)
@Composable
fun TmpDatePickerLook() =
    DsTheme(animationsEnabled = false) {
        Column(Modifier.background(DsTheme.colors.surface).padding(8.dp)) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = FEB28, initialDisplayedMonthMillis = FEB28),
                colors = DatePickerDefaults.colors(containerColor = DsTheme.colors.surface),
            )
            Row(
                Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                DsButton("Annuler", variant = DsButtonVariant.Ghost, size = DsButtonSize.Small, onClick = {})
                DsButton("Valider", size = DsButtonSize.Small, onClick = {})
            }
            DateRangePicker(
                state =
                    rememberDateRangePickerState(
                        initialSelectedStartDateMillis = FEB28,
                        initialSelectedEndDateMillis = MAR06,
                        initialDisplayedMonthMillis = FEB28,
                    ),
                colors = DatePickerDefaults.colors(containerColor = DsTheme.colors.surface),
            )
        }
    }
