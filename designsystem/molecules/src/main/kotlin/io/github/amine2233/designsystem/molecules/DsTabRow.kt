package io.github.amine2233.designsystem.molecules

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Underlined tabs (Catalogue groups, orders filters). For a toggle use [DsSegmentedControl]. */
@Composable
fun DsTabRow(
    tabs: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = DsTheme.colors
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = c.surface,
        contentColor = c.primary,
        indicator = { positions ->
            TabRowDefaults.SecondaryIndicator(Modifier.tabIndicatorOffset(positions[selectedIndex]), height = 2.dp, color = c.primary)
        },
        divider = { androidx.compose.material3.HorizontalDivider(color = c.border) },
    ) {
        tabs.forEachIndexed { i, label ->
            Tab(
                selected = i == selectedIndex,
                onClick = { onSelect(i) },
                selectedContentColor = c.primary,
                unselectedContentColor = c.textSecondary,
                text = {
                    Text(
                        label,
                        style =
                            if (i ==
                                selectedIndex
                            ) {
                                DsTheme.typography.labelStrong
                            } else {
                                DsTheme.typography.label
                            },
                        maxLines = 1,
                    )
                },
            )
        }
    }
}

@DsComponentPreview
@Composable
private fun DsTabRowPreview() =
    DsPreview {
        DsTabRow(listOf("Boissons", "Nourriture", "Extras"), 0, {})
    }
