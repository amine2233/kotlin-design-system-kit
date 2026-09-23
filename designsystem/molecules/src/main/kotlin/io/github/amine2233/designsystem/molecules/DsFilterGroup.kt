package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import io.github.amine2233.designsystem.atoms.DsChip
import io.github.amine2233.designsystem.atoms.DsChipTone
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Whether a filter group lets one option through at a time or several. */
enum class DsFilterSelection { Single, Multiple }

/** One line that scrolls, or several that wrap — same trade-off as a tag group. */
enum class DsFilterLayout { Scroll, Wrap }

/**
 * What tapping option [index] leaves selected.
 *
 * [DsFilterSelection.Single] replaces the selection and never empties it by re-tapping — the user
 * would be left with no filter and no idea why the list changed; clearing is the "all" chip's job.
 * [DsFilterSelection.Multiple] toggles, so a second tap removes that one.
 */
fun dsToggleFilter(
    selected: Set<Int>,
    index: Int,
    selection: DsFilterSelection,
): Set<Int> =
    when (selection) {
        DsFilterSelection.Single -> setOf(index)
        DsFilterSelection.Multiple -> if (index in selected) selected - index else selected + index
    }

/**
 * Chips the user filters a list with — categories, order states, payment methods.
 *
 * The selection crosses the boundary as indices into [options], so the caller keeps its own type.
 * [allLabel] renders a leading chip that clears the selection; it is selected exactly when nothing
 * else is, which is also what an empty filter means, so the bar always has one chip lit.
 *
 * For tags that only describe, use `DsTagGroup` — a chip that cannot be tapped reads as a broken
 * filter.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DsFilterGroup(
    options: List<String>,
    selected: Set<Int>,
    onSelectedChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    selection: DsFilterSelection = DsFilterSelection.Multiple,
    layout: DsFilterLayout = DsFilterLayout.Scroll,
    allLabel: String? = null,
    tone: DsChipTone = DsChipTone.Neutral,
    enabled: Boolean = true,
    spacing: Dp = DsTheme.spacing.sm,
) {
    val chips: @Composable () -> Unit = {
        if (allLabel != null) {
            DsChip(
                allLabel,
                selected = selected.isEmpty(),
                tone = tone,
                onClick = if (enabled) ({ onSelectedChange(emptySet()) }) else null,
            )
        }
        options.forEachIndexed { index, label ->
            DsChip(
                label,
                selected = index in selected,
                tone = tone,
                onClick = if (enabled) ({ onSelectedChange(dsToggleFilter(selected, index, selection)) }) else null,
            )
        }
    }
    when (layout) {
        DsFilterLayout.Scroll -> {
            Row(
                modifier = modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(spacing),
            ) { chips() }
        }

        DsFilterLayout.Wrap -> {
            FlowRow(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing),
            ) { chips() }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsFilterGroupPreview() =
    DsPreview {
        DsFilterGroup(
            listOf("Chauds", "Froids", "Snacks", "Desserts"),
            selected = setOf(0),
            onSelectedChange = {},
            selection = DsFilterSelection.Single,
            allLabel = "Tous",
        )
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsFilterGroup(
            listOf("Carte", "Espèces", "Mixte", "En attente", "Remboursé"),
            selected = setOf(0, 2),
            onSelectedChange = {},
            layout = DsFilterLayout.Wrap,
        )
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsFilterGroup(
            listOf("Chauds", "Froids"),
            selected = emptySet(),
            onSelectedChange = {},
            allLabel = "Tous",
            enabled = false,
        )
    }
