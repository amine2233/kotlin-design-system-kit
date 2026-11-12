package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import io.github.amine2233.designsystem.atoms.DsChip
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** What a [DsTipSelector] has selected: no tip, a whole-number percentage, or an amount in minor units. */
sealed interface DsTipSelection {
    data object None : DsTipSelection

    data class Preset(
        val percent: Int,
    ) : DsTipSelection

    data class Custom(
        val amountMinor: Long,
    ) : DsTipSelection
}

/**
 * The tip for [selection] on [subtotalMinor], in minor units.
 *
 * A preset is rounded half up to the nearest minor unit with integer arithmetic only; the subtotal is
 * split into hundreds and remainder first, so no step overflows for any subtotal at up to 100 %.
 * A negative subtotal, percent or custom amount counts as 0.
 */
fun dsTipAmount(
    subtotalMinor: Long,
    selection: DsTipSelection,
): Long =
    when (selection) {
        DsTipSelection.None -> {
            0L
        }

        is DsTipSelection.Custom -> {
            selection.amountMinor.coerceAtLeast(0L)
        }

        is DsTipSelection.Preset -> {
            val subtotal = subtotalMinor.coerceAtLeast(0L)
            val percent = selection.percent.coerceAtLeast(0).toLong()
            subtotal / 100 * percent + (subtotal % 100 * percent + 50) / 100
        }
    }

/** The subtotal (at least 0) plus [dsTipAmount], in minor units. */
fun dsTipTotal(
    subtotalMinor: Long,
    selection: DsTipSelection,
): Long = subtotalMinor.coerceAtLeast(0L) + dsTipAmount(subtotalMinor, selection)

/**
 * A single-choice tip picker — "none", each of [presets] and "custom" — followed by the tip and the
 * new total for the current [selection].
 *
 * Amounts are minor units and [formatAmount] turns them into text, so currency stays the caller's.
 * "Custom" only reports that it was chosen, keeping the current amount or starting at 0; the caller
 * collects the digits (a `DsNumericKeypad` on the screen, say) and passes them back in [selection].
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DsTipSelector(
    subtotalMinor: Long,
    selection: DsTipSelection,
    onSelectionChange: (DsTipSelection) -> Unit,
    formatAmount: (Long) -> String,
    noneLabel: String,
    customLabel: String,
    tipLabel: String,
    totalLabel: String,
    modifier: Modifier = Modifier,
    presets: List<Int> = listOf(10, 15, 20),
    presetLabel: (Int) -> String = { "$it %" },
) {
    val customSelection = selection as? DsTipSelection.Custom ?: DsTipSelection.Custom(0L)
    val options =
        buildList {
            add(Triple(noneLabel, selection == DsTipSelection.None, DsTipSelection.None))
            presets.forEach { percent ->
                val preset = DsTipSelection.Preset(percent)
                add(Triple(presetLabel(percent), selection == preset, preset))
            }
            add(Triple(customLabel, selection is DsTipSelection.Custom, customSelection))
        }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
    ) {
        FlowRow(
            modifier = Modifier.fillMaxWidth().selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
            verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
        ) {
            options.forEach { (label, isSelected, option) ->
                DsChip(
                    label,
                    modifier =
                        Modifier
                            .heightIn(min = DsTheme.spacing.minTouchTarget)
                            .clip(DsTheme.shapes.chip)
                            .selectable(selected = isSelected, role = Role.RadioButton, onClick = { onSelectionChange(option) }),
                    selected = isSelected,
                    onClick = null,
                )
            }
        }
        DsTotalsRow(
            tipLabel,
            formatAmount(dsTipAmount(subtotalMinor, selection)),
            modifier = Modifier.semantics(mergeDescendants = true) {},
            emphasis = DsTotalsEmphasis.Positive,
        )
        DsTotalsRow(
            totalLabel,
            formatAmount(dsTipTotal(subtotalMinor, selection)),
            modifier = Modifier.semantics(mergeDescendants = true) {},
            emphasis = DsTotalsEmphasis.Total,
        )
    }
}

private fun previewAmount(minor: Long): String = "${minor / 100},${(minor % 100).toString().padStart(2, '0')} DA"

@DsComponentPreview
@Composable
private fun DsTipSelectorPreview() =
    DsPreview {
        listOf(
            108_000L to DsTipSelection.None,
            108_000L to DsTipSelection.Preset(10),
            108_000L to DsTipSelection.Custom(25_000L),
            0L to DsTipSelection.Preset(15),
        ).forEach { (subtotal, selection) ->
            DsTipSelector(
                subtotal,
                selection,
                onSelectionChange = {},
                formatAmount = ::previewAmount,
                noneLabel = "Aucun",
                customLabel = "Personnalisé",
                tipLabel = "Pourboire",
                totalLabel = "Nouveau total",
            )
            Spacer(Modifier.height(DsTheme.spacing.md))
        }
        DsTipSelector(
            108_000L,
            DsTipSelection.Preset(18),
            onSelectionChange = {},
            formatAmount = ::previewAmount,
            noneLabel = "Aucun",
            customLabel = "Personnalisé",
            tipLabel = "Pourboire",
            totalLabel = "Nouveau total",
            presets = listOf(5, 8, 10, 12, 15, 18, 20, 25),
        )
    }
