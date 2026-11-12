package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.disabled
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsQuantityStepper
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.molecules.DsTotalsRow

/** One line of a past order, with [unitPriceMinor] the unit price actually paid in minor units. */
@Immutable
data class DsRefundLine(
    val name: String,
    val unitPriceMinor: Long,
    val quantityBought: Int,
    val quantityRefunded: Int = 0,
) {
    init {
        require(unitPriceMinor >= 0) { "unitPriceMinor must not be negative: $unitPriceMinor" }
        require(quantityBought >= 0) { "quantityBought must not be negative: $quantityBought" }
        require(quantityRefunded >= 0) { "quantityRefunded must not be negative: $quantityRefunded" }
        require(quantityRefunded <= quantityBought) {
            "quantityRefunded ($quantityRefunded) must not exceed quantityBought ($quantityBought)"
        }
    }
}

fun dsRefundableQuantity(line: DsRefundLine): Int = line.quantityBought - line.quantityRefunded

/** [selected] capped at [dsRefundableQuantity]; a negative [selected] throws. */
fun dsClampRefundQuantity(
    line: DsRefundLine,
    selected: Int,
): Int {
    require(selected >= 0) { "selected must not be negative: $selected" }
    return selected.coerceAtMost(dsRefundableQuantity(line))
}

/** Unit price times the clamped quantity, in minor units. Overflow throws [ArithmeticException]. */
fun dsRefundLineAmount(
    line: DsRefundLine,
    selected: Int,
): Long = Math.multiplyExact(line.unitPriceMinor, dsClampRefundQuantity(line, selected).toLong())

/** Sum of the line amounts in minor units. Overflow throws [ArithmeticException]. */
fun dsRefundTotal(
    lines: List<DsRefundLine>,
    selectedQuantities: List<Int>,
): Long {
    require(lines.size == selectedQuantities.size) {
        "lines (${lines.size}) and selectedQuantities (${selectedQuantities.size}) must have the same size"
    }
    return lines.zip(selectedQuantities).fold(0L) { total, (line, selected) ->
        Math.addExact(total, dsRefundLineAmount(line, selected))
    }
}

/** True when the refund total is above 0: the caller enables its confirm button with it, like [isSufficient]. */
fun dsHasRefundSelection(
    lines: List<DsRefundLine>,
    selectedQuantities: List<Int>,
): Boolean = dsRefundTotal(lines, selectedQuantities) > 0

/**
 * Refund item selection: each line of a past order with a stepper for the units to refund, then the refund total.
 *
 * Stateless. The caller owns [selectedQuantities], parallel to [lines]: [onQuantityChange] gives the 0-based
 * `index` in [lines] and the new quantity, already clamped to what is refundable. Fully refunded lines are
 * disabled. Use [dsHasRefundSelection] to enable the caller's own confirm button.
 */
@Composable
fun DsRefundSelection(
    lines: List<DsRefundLine>,
    selectedQuantities: List<Int>,
    onQuantityChange: (index: Int, quantity: Int) -> Unit,
    unit: String,
    modifier: Modifier = Modifier,
    refundTotalLabel: String = "Refund total",
    refundableLabel: (Int) -> String = { "$it refundable" },
    fullyRefundedLabel: String = "Fully refunded",
    emptyLabel: String = "Select items to refund",
) {
    require(lines.size == selectedQuantities.size) {
        "lines (${lines.size}) and selectedQuantities (${selectedQuantities.size}) must have the same size"
    }
    val c = DsTheme.colors
    val spacing = DsTheme.spacing
    val hasSelection = dsHasRefundSelection(lines, selectedQuantities)
    val value = "${dsFormatMinorUnits(dsRefundTotal(lines, selectedQuantities))} $unit"
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(spacing.sm)) {
        lines.forEachIndexed { index, line ->
            if (index > 0) DsDivider()
            RefundLineRow(
                line = line,
                selected = selectedQuantities[index],
                onQuantityChange = { onQuantityChange(index, it) },
                unit = unit,
                refundableLabel = refundableLabel,
                fullyRefundedLabel = fullyRefundedLabel,
            )
        }
        if (!hasSelection) {
            Text(emptyLabel, style = DsTheme.typography.label, color = c.textSecondary)
        }
        DsTotalsRow(
            refundTotalLabel,
            value,
            Modifier.clearAndSetSemantics {
                contentDescription = "$refundTotalLabel $value"
                liveRegion = LiveRegionMode.Polite
            },
            emphasis = DsTotalsEmphasis.Total,
            valueColor = if (hasSelection) null else c.textSecondary,
        )
    }
}

@Composable
private fun RefundLineRow(
    line: DsRefundLine,
    selected: Int,
    onQuantityChange: (Int) -> Unit,
    unit: String,
    refundableLabel: (Int) -> String,
    fullyRefundedLabel: String,
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val refundable = dsRefundableQuantity(line)
    val disabled = refundable == 0
    Row(
        modifier = Modifier.fillMaxWidth().heightIn(min = DsTheme.spacing.minTouchTarget),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(end = DsTheme.spacing.smd)
                    .semantics(mergeDescendants = true) { if (disabled) disabled() },
            verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
        ) {
            Text(line.name, style = t.bodyStrong, color = if (disabled) c.textDisabled else c.textPrimary)
            Text(
                "${dsFormatMinorUnits(line.unitPriceMinor)} $unit",
                style = t.label,
                color = if (disabled) c.textDisabled else c.textSecondary,
            )
            Text(
                if (disabled) fullyRefundedLabel else refundableLabel(refundable),
                style = t.caption,
                color = if (disabled) c.textDisabled else c.textSecondary,
            )
        }
        DsQuantityStepper(
            quantity = dsClampRefundQuantity(line, selected),
            onQuantityChange = { onQuantityChange(dsClampRefundQuantity(line, it)) },
            min = 0,
            max = refundable,
        )
    }
}

private val previewLines =
    listOf(
        DsRefundLine("Cappuccino", unitPriceMinor = 20_000, quantityBought = 2),
        DsRefundLine("Croissant", unitPriceMinor = 9_000, quantityBought = 3, quantityRefunded = 1),
    )

@Composable
private fun RefundSelectionPreview(
    selectedQuantities: List<Int>,
    lines: List<DsRefundLine> = previewLines,
) = DsRefundSelection(
    lines = lines,
    selectedQuantities = selectedQuantities,
    onQuantityChange = { _, _ -> },
    unit = "DA",
)

@DsComponentPreview
@Composable
private fun DsRefundSelectionEmptyPreview() = DsPreview { RefundSelectionPreview(listOf(0, 0)) }

@DsComponentPreview
@Composable
private fun DsRefundSelectionPartialPreview() = DsPreview { RefundSelectionPreview(listOf(1, 0)) }

@DsComponentPreview
@Composable
private fun DsRefundSelectionFullPreview() = DsPreview { RefundSelectionPreview(listOf(2, 2)) }

@DsComponentPreview
@Composable
private fun DsRefundSelectionFullyRefundedLinePreview() =
    DsPreview {
        RefundSelectionPreview(
            listOf(1, 0, 0),
            previewLines + DsRefundLine("Espresso", unitPriceMinor = 15_000, quantityBought = 1, quantityRefunded = 1),
        )
    }
