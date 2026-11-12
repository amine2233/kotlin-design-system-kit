package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.atoms.DsTag
import io.github.amine2233.designsystem.atoms.DsTagTone
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsAllocation
import io.github.amine2233.designsystem.molecules.DsAllocationBar
import io.github.amine2233.designsystem.molecules.DsQuantityStepper
import io.github.amine2233.designsystem.molecules.DsSegmentedControl
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.molecules.DsTotalsRow

/** How a [DsSplitBill] divides the total: equal shares, or an amount set per part. */
enum class DsSplitMode { Equal, Custom }

/**
 * One part of a split bill.
 *
 * [amountMinor] is the part's amount in minor units in [DsSplitMode.Custom] and is ignored in
 * [DsSplitMode.Equal], where every amount is derived from the total. A null [label] falls back to the
 * `partLabel` of [DsSplitBill].
 */
@Immutable
data class DsSplitPart(
    val paid: Boolean = false,
    val amountMinor: Long = 0L,
    val label: String? = null,
)

/**
 * [parts] shares of [totalMinor] that sum exactly to it: each is `total / parts`, and the first
 * `total % parts` get one more minor unit. A negative total counts as 0; `parts <= 0` gives no share.
 */
fun dsEqualSplit(
    totalMinor: Long,
    parts: Int,
): List<Long> {
    if (parts <= 0) return emptyList()
    val total = totalMinor.coerceAtLeast(0L)
    val share = total / parts
    val remainder = total % parts
    return List(parts) { index -> if (index < remainder) share + 1 else share }
}

/** The amount shown for each of [parts]: equal shares of [totalMinor], or each custom amount (negatives count as 0). */
fun dsSplitAmounts(
    totalMinor: Long,
    mode: DsSplitMode,
    parts: List<DsSplitPart>,
): List<Long> =
    when (mode) {
        DsSplitMode.Equal -> dsEqualSplit(totalMinor, parts.size)
        DsSplitMode.Custom -> parts.map { it.amountMinor.coerceAtLeast(0L) }
    }

/**
 * [totalMinor] minus the sum of [amountsMinor]: positive is unallocated, negative is over-allocated,
 * 0 is balanced. Negative inputs count as 0.
 */
fun dsSplitUnallocated(
    totalMinor: Long,
    amountsMinor: List<Long>,
): Long = totalMinor.coerceAtLeast(0L) - amountsMinor.sumOf { it.coerceAtLeast(0L) }

/** The sum of [amountsMinor] whose matching entry in [parts] is paid. */
fun dsSplitPaid(
    amountsMinor: List<Long>,
    parts: List<DsSplitPart>,
): Long = amountsMinor.zip(parts).sumOf { (amount, part) -> if (part.paid) amount else 0L }

/**
 * Splits [totalMinor] into [parts], by equal shares or by custom amounts, and shows each part as paid
 * or remaining with a collect action, then the total, the paid share and what is left to pay.
 *
 * Amounts are minor units and [formatAmount] turns them into text, so currency stays the caller's.
 * The caller owns [parts]: it resizes the list on [onPartCountChange], marks a part paid on
 * [onCollect], and in Custom mode opens its own amount entry on [onPartClick]. Index arguments are
 * 0-based positions in [parts]. Collecting is disabled while Custom amounts exceed the total.
 */
@Composable
fun DsSplitBill(
    totalMinor: Long,
    mode: DsSplitMode,
    onModeChange: (DsSplitMode) -> Unit,
    parts: List<DsSplitPart>,
    onPartCountChange: (Int) -> Unit,
    maxParts: Int,
    onPartClick: (Int) -> Unit,
    onCollect: (Int) -> Unit,
    formatAmount: (Long) -> String,
    equalModeLabel: String,
    customModeLabel: String,
    partCountLabel: String,
    totalLabel: String,
    paidLabel: String,
    remainingLabel: String,
    unallocatedLabel: String,
    overAllocatedLabel: String,
    collectLabel: String,
    modifier: Modifier = Modifier,
    partLabel: (Int) -> String = { "Part ${it + 1}" },
) {
    val c = DsTheme.colors
    val total = totalMinor.coerceAtLeast(0L)
    val amounts = dsSplitAmounts(total, mode, parts)
    val paid = dsSplitPaid(amounts, parts)
    val unallocated = if (mode == DsSplitMode.Custom) dsSplitUnallocated(total, amounts) else 0L
    val canCollect = unallocated >= 0L
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
    ) {
        DsSegmentedControl(
            options = listOf(equalModeLabel, customModeLabel),
            selectedIndex = mode.ordinal,
            onSelect = { onModeChange(DsSplitMode.entries[it]) },
            modifier = Modifier.fillMaxWidth(),
        )
        if (mode == DsSplitMode.Equal) {
            Row(
                modifier = Modifier.fillMaxWidth().heightIn(min = DsTheme.spacing.minTouchTarget),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(partCountLabel, modifier = Modifier.weight(1f), style = DsTheme.typography.body, color = c.textPrimary)
                DsQuantityStepper(parts.size, onPartCountChange, min = 2, max = maxParts)
            }
        }
        parts.forEachIndexed { index, part ->
            if (index > 0) DsDivider()
            SplitPartRow(
                label = part.label ?: partLabel(index),
                amount = formatAmount(amounts[index]),
                paid = part.paid,
                paidLabel = paidLabel,
                remainingLabel = remainingLabel,
                collectLabel = collectLabel,
                canCollect = canCollect,
                onClick = if (mode == DsSplitMode.Custom && !part.paid) ({ onPartClick(index) }) else null,
                onCollect = { onCollect(index) },
            )
        }
        if (unallocated > 0L) {
            DsTotalsRow(
                unallocatedLabel,
                formatAmount(unallocated),
                modifier = Modifier.semantics(mergeDescendants = true) {},
            )
        } else if (unallocated < 0L) {
            DsTotalsRow(
                overAllocatedLabel,
                formatAmount(-unallocated),
                modifier = Modifier.semantics(mergeDescendants = true) {},
                valueColor = c.error,
            )
        }
        DsAllocationBar(
            listOf(
                DsAllocation(
                    "$paidLabel ${formatAmount(paid)}",
                    if (total == 0L) 0f else (paid.toFloat() / total.toFloat()).coerceIn(0f, 1f),
                    c.success,
                ),
            ),
            modifier = Modifier.padding(vertical = DsTheme.spacing.xs),
        )
        DsTotalsRow(
            totalLabel,
            formatAmount(total),
            modifier = Modifier.semantics(mergeDescendants = true) {},
            emphasis = DsTotalsEmphasis.Total,
        )
        DsTotalsRow(
            paidLabel,
            formatAmount(paid),
            modifier = Modifier.semantics(mergeDescendants = true) {},
            emphasis = DsTotalsEmphasis.Positive,
            valueColor = c.success,
        )
        DsTotalsRow(
            remainingLabel,
            formatAmount((total - paid).coerceAtLeast(0L)),
            modifier = Modifier.semantics(mergeDescendants = true) {},
        )
    }
}

@Composable
private fun SplitPartRow(
    label: String,
    amount: String,
    paid: Boolean,
    paidLabel: String,
    remainingLabel: String,
    collectLabel: String,
    canCollect: Boolean,
    onClick: (() -> Unit)?,
    onCollect: () -> Unit,
) {
    val c = DsTheme.colors
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = DsTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.smd),
    ) {
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .heightIn(min = DsTheme.spacing.minTouchTarget)
                    .clip(DsTheme.shapes.sm)
                    .then(if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier)
                    .semantics(mergeDescendants = true) {},
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.smd),
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs)) {
                Text(label, style = DsTheme.typography.bodyStrong, color = c.textPrimary, maxLines = 1)
                DsTag(
                    if (paid) paidLabel else remainingLabel,
                    tone = if (paid) DsTagTone.Success else DsTagTone.Neutral,
                )
            }
            Text(amount, style = DsTheme.typography.title, color = c.textPrimary)
        }
        if (!paid) {
            DsButton(
                collectLabel,
                onClick = onCollect,
                modifier = Modifier.heightIn(min = DsTheme.spacing.minTouchTarget),
                size = DsButtonSize.Medium,
                enabled = canCollect,
            )
        }
    }
}

private fun previewAmount(minor: Long): String = "${minor / 100},${(minor % 100).toString().padStart(2, '0')} DA"

@Composable
private fun PreviewSplitBill(
    mode: DsSplitMode,
    parts: List<DsSplitPart>,
    totalMinor: Long = 100_000L,
) = DsSplitBill(
    totalMinor = totalMinor,
    mode = mode,
    onModeChange = {},
    parts = parts,
    onPartCountChange = {},
    maxParts = 8,
    onPartClick = {},
    onCollect = {},
    formatAmount = ::previewAmount,
    equalModeLabel = "Parts égales",
    customModeLabel = "Montants libres",
    partCountLabel = "Nombre de parts",
    totalLabel = "Total",
    paidLabel = "Payé",
    remainingLabel = "Reste à payer",
    unallocatedLabel = "Non réparti",
    overAllocatedLabel = "Dépassement",
    collectLabel = "Encaisser",
)

@DsComponentPreview
@Composable
private fun DsSplitBillEqualPreview() =
    DsPreview {
        PreviewSplitBill(DsSplitMode.Equal, List(3) { DsSplitPart() })
        Spacer(Modifier.height(DsTheme.spacing.lg))
        PreviewSplitBill(DsSplitMode.Equal, listOf(DsSplitPart(paid = true), DsSplitPart(), DsSplitPart()))
        Spacer(Modifier.height(DsTheme.spacing.lg))
        PreviewSplitBill(DsSplitMode.Equal, List(2) { DsSplitPart(paid = true) })
    }

@DsComponentPreview
@Composable
private fun DsSplitBillCustomPreview() =
    DsPreview {
        PreviewSplitBill(
            DsSplitMode.Custom,
            listOf(DsSplitPart(paid = true, amountMinor = 60_000L, label = "Karim"), DsSplitPart(amountMinor = 40_000L, label = "Sonia")),
        )
        Spacer(Modifier.height(DsTheme.spacing.lg))
        PreviewSplitBill(DsSplitMode.Custom, listOf(DsSplitPart(paid = true, amountMinor = 50_000L), DsSplitPart(amountMinor = 30_000L)))
        Spacer(Modifier.height(DsTheme.spacing.lg))
        PreviewSplitBill(DsSplitMode.Custom, listOf(DsSplitPart(amountMinor = 70_000L), DsSplitPart(amountMinor = 45_000L)))
    }
