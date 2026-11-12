package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Whether a selection list is a one-of chooser (radios) or a several-of chooser (checkboxes). */
enum class DsSelectionListMode { Single, Multiple }

/**
 * Which side of the label the radio or checkbox sits on. [Start] and [End] follow the layout
 * direction, so an RTL screen mirrors them without the caller doing anything.
 */
enum class DsSelectionIndicatorPlacement { Start, End }

/**
 * What tapping option [index] leaves selected.
 *
 * The rule is `dsToggleFilter`'s, so a selection list and a `DsFilterGroup` can never disagree:
 * [DsSelectionListMode.Single] replaces the selection and a re-tap keeps it, [DsSelectionListMode.Multiple]
 * toggles.
 */
fun dsChooseOption(
    selected: Set<Int>,
    index: Int,
    mode: DsSelectionListMode,
): Set<Int> =
    dsToggleFilter(
        selected,
        index,
        when (mode) {
            DsSelectionListMode.Single -> DsFilterSelection.Single
            DsSelectionListMode.Multiple -> DsFilterSelection.Multiple
        },
    )

/**
 * A selection staged before the user applies or cancels it — the ticks in a filter sheet that only
 * count once "Appliquer" is tapped.
 *
 * The caller holds it next to the committed selection: it feeds [selected] to a `DsSelectionList`,
 * replaces it with [choose] on every tap, applies by copying [selected] into the committed state and
 * cancels by dropping it. Immutable, so a discarded draft can never leak into the committed value.
 */
data class DsSelectionDraft(
    val mode: DsSelectionListMode,
    val selected: Set<Int>,
) {
    /** The draft after tapping option [index]; the receiver is left as it was. */
    fun choose(index: Int): DsSelectionDraft = copy(selected = dsChooseOption(selected, index, mode))

    companion object {
        /**
         * A draft starting from the [committed] selection. [DsSelectionListMode.Single] keeps at most
         * one index — the lowest — so a stray multi-index set cannot show two radios lit.
         */
        fun from(
            committed: Set<Int>,
            mode: DsSelectionListMode,
        ): DsSelectionDraft =
            DsSelectionDraft(
                mode = mode,
                selected =
                    when (mode) {
                        DsSelectionListMode.Single -> committed.minOrNull()?.let { setOf(it) } ?: emptySet()
                        DsSelectionListMode.Multiple -> committed
                    },
            )
    }
}

/**
 * A short, always-visible list of text options with a radio ([DsSelectionListMode.Single]) or a
 * checkbox ([DsSelectionListMode.Multiple]) on each row — a sort order, the categories a report covers.
 *
 * The selection crosses the boundary as indices into [options], so the caller keeps its own type.
 * The whole row is the touch target and the only focus stop; the indicator draws state and nothing
 * else. The list draws no title, no buttons and no sheet: for apply/cancel, drive it from a
 * [DsSelectionDraft] and put the buttons where the screen wants them.
 *
 * For options hidden behind a field, use `DsDropdown` or `DsMultiSelectField`; for a chip bar that
 * filters a list, `DsFilterGroup`.
 */
@Composable
fun DsSelectionList(
    options: List<String>,
    selected: Set<Int>,
    onSelectedChange: (Set<Int>) -> Unit,
    modifier: Modifier = Modifier,
    mode: DsSelectionListMode = DsSelectionListMode.Single,
    indicatorPlacement: DsSelectionIndicatorPlacement = DsSelectionIndicatorPlacement.Start,
    enabled: Boolean = true,
) {
    val c = DsTheme.colors
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .then(if (mode == DsSelectionListMode.Single) Modifier.selectableGroup() else Modifier),
    ) {
        options.forEachIndexed { index, label ->
            val isSelected = index in selected
            val onChoose = { onSelectedChange(dsChooseOption(selected, index, mode)) }
            val interaction =
                when (mode) {
                    DsSelectionListMode.Single -> {
                        Modifier.selectable(selected = isSelected, enabled = enabled, role = Role.RadioButton, onClick = onChoose)
                    }

                    DsSelectionListMode.Multiple -> {
                        Modifier.toggleable(value = isSelected, enabled = enabled, role = Role.Checkbox, onValueChange = { onChoose() })
                    }
                }
            val indicator: @Composable () -> Unit = {
                when (mode) {
                    DsSelectionListMode.Single -> {
                        RadioButton(
                            selected = isSelected,
                            onClick = null,
                            enabled = enabled,
                            colors = RadioButtonDefaults.colors(selectedColor = c.primary, unselectedColor = c.borderStrong),
                        )
                    }

                    DsSelectionListMode.Multiple -> {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = null,
                            enabled = enabled,
                            colors =
                                CheckboxDefaults.colors(
                                    checkedColor = c.primary,
                                    uncheckedColor = c.borderStrong,
                                    checkmarkColor = c.onPrimary,
                                ),
                        )
                    }
                }
            }
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = DsTheme.spacing.minTouchTarget)
                        .then(interaction)
                        .padding(horizontal = DsTheme.spacing.md),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.md),
            ) {
                if (indicatorPlacement == DsSelectionIndicatorPlacement.Start) indicator()
                Text(
                    label,
                    modifier = Modifier.weight(1f),
                    style = DsTheme.typography.body,
                    color = if (enabled) c.textPrimary else c.textDisabled,
                )
                if (indicatorPlacement == DsSelectionIndicatorPlacement.End) indicator()
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun SelectionListStatesPreview() =
    DsPreview {
        DsSelectionList(
            listOf("Nom", "Prix croissant", "Prix décroissant"),
            selected = setOf(0),
            onSelectedChange = {},
        )
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsSelectionList(
            listOf("Chauds", "Froids", "Snacks", "Desserts"),
            selected = setOf(0, 2),
            onSelectedChange = {},
            mode = DsSelectionListMode.Multiple,
            indicatorPlacement = DsSelectionIndicatorPlacement.End,
        )
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsSelectionList(
            listOf("Carte", "Espèces"),
            selected = setOf(1),
            onSelectedChange = {},
            enabled = false,
        )
    }
