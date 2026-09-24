package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsChip
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * How a [DsChipRow] fills its width.
 *
 * [Inline] keeps one line and scrolls, so a long category list stays on a single row. [Grid] wraps
 * onto as many lines as it needs and never scrolls — every option visible at once. Chips keep their
 * text width in both: stretching them to equal columns would make "0%" as wide as "Pâtisserie".
 *
 * DsChipRow's own enum, matching DsFilterLayout and DsTagLayout in meaning without being shared
 * with them: this row can gain a layout that a tag group should never have.
 */
enum class DsChipRowLayout { Inline, Grid }

/** Single-select chips (categories, TVA rates, quick tips). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DsChipRow(
    options: List<String>,
    selectedIndex: Int?,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    layout: DsChipRowLayout = DsChipRowLayout.Inline,
) {
    val inline = layout == DsChipRowLayout.Inline
    val chips: @Composable () -> Unit = {
        options.forEachIndexed { i, label ->
            DsChip(
                label,
                selected = i == selectedIndex,
                onClick = { onSelect(i) },
            )
        }
    }
    if (inline) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) { chips() }
    } else {
        FlowRow(
            modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) { chips() }
    }
}

@DsComponentPreview
@Composable
private fun DsChipRowPreview() =
    DsPreview {
        DsChipRow(listOf("Tous", "Chauds", "Froids", "Snacks", "Pâtisserie", "Extras"), 0, {})
        DsChipRow(listOf("20%", "10%", "5,5%", "0%"), 1, {}, layout = DsChipRowLayout.Grid)
    }
