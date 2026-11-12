package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsStroke
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsQuantityStepper
import io.github.amine2233.designsystem.molecules.DsTotalsEmphasis
import io.github.amine2233.designsystem.molecules.DsTotalsRow

/** One guest of a [DsSplitByItem]. A null [label] falls back to the `guestLabel` of [DsSplitByItem]. */
@Immutable
data class DsSplitGuest(
    val label: String? = null,
)

/**
 * One bill line split by item: [quantity] whole units worth [totalMinor] minor units together.
 *
 * `assignedQuantities[g]` is the number of units guest `g` (0-based in the guest list) takes. A missing
 * entry counts as 0 and entries beyond the guest count are ignored. Bad input is clamped by
 * [dsSplitItemAssigned], never thrown.
 */
@Immutable
data class DsSplitItemLine(
    val name: String,
    val quantity: Int,
    val totalMinor: Long,
    val assignedQuantities: List<Int> = emptyList(),
)

/**
 * The units each of [guestCount] guests holds on [line], after clamping: a negative quantity or
 * assigned value counts as 0, then guests are served in index order and each gets
 * `min(requested, units still free)`, so over-assigned input is cut back from the last guests.
 */
fun dsSplitItemAssigned(
    line: DsSplitItemLine,
    guestCount: Int,
): List<Int> {
    var free = line.quantity.coerceAtLeast(0)
    return List(guestCount.coerceAtLeast(0)) { guest ->
        val taken = line.assignedQuantities.getOrElse(guest) { 0 }.coerceIn(0, free)
        free -= taken
        taken
    }
}

/** The units of [line] no guest holds: `max(quantity, 0)` minus the sum of [dsSplitItemAssigned], never negative. */
fun dsSplitItemFreeUnits(
    line: DsSplitItemLine,
    guestCount: Int,
): Int = line.quantity.coerceAtLeast(0) - dsSplitItemAssigned(line, guestCount).sum()

/**
 * What each guest owes for [line], in minor units.
 *
 * The units carry [dsEqualSplit] of the line total (negatives count as 0), so the first `total % quantity`
 * units are one minor unit higher. Units are handed out in order to guests in index order: guest 0 takes
 * the first units it holds, guest 1 the next ones, and so on. Assignments are clamped as in
 * [dsSplitItemAssigned]; once every unit is assigned the shares sum exactly to the line total.
 */
fun dsSplitItemShares(
    line: DsSplitItemLine,
    guestCount: Int,
): List<Long> {
    val units = dsEqualSplit(line.totalMinor, line.quantity)
    var next = 0
    return dsSplitItemAssigned(line, guestCount).map { taken ->
        val share = units.subList(next, next + taken).sum()
        next += taken
        share
    }
}

/** Each guest's sum of [dsSplitItemShares] across [lines], with the same clamp rule and remainder order. */
fun dsSplitItemGuestSubtotals(
    lines: List<DsSplitItemLine>,
    guestCount: Int,
): List<Long> {
    val subtotals = LongArray(guestCount.coerceAtLeast(0))
    lines.forEach { line ->
        dsSplitItemShares(line, guestCount).forEachIndexed { guest, share -> subtotals[guest] += share }
    }
    return subtotals.toList()
}

/** The sum of the line totals, negatives counting as 0. */
fun dsSplitItemTotal(lines: List<DsSplitItemLine>): Long = lines.sumOf { it.totalMinor.coerceAtLeast(0L) }

/**
 * [dsSplitItemTotal] minus the sum of [dsSplitItemGuestSubtotals], never negative. A line with
 * `quantity <= 0` cannot be assigned, so its whole clamped total counts as unassigned.
 */
fun dsSplitItemUnassigned(
    lines: List<DsSplitItemLine>,
    guestCount: Int,
): Long = (dsSplitItemTotal(lines) - dsSplitItemGuestSubtotals(lines, guestCount).sum()).coerceAtLeast(0L)

/**
 * Splits a bill by item: the cashier picks one of [guests], then sets with a stepper how many whole
 * units of each line that guest takes. Shows each guest's subtotal, then the total, the assigned share
 * and what is still unassigned.
 *
 * Stateless. The caller owns each line's `assignedQuantities` and [selectedGuest]: [onSelectGuest] gives a
 * 0-based index in [guests], and [onAssign] gives the 0-based line and guest indices with the new
 * quantity, already clamped to what that guest can take. A [selectedGuest] outside [guests] selects
 * nobody and hides the steppers. Amounts are minor units and [formatAmount] turns them into text.
 */
@Composable
fun DsSplitByItem(
    lines: List<DsSplitItemLine>,
    guests: List<DsSplitGuest>,
    selectedGuest: Int,
    onSelectGuest: (Int) -> Unit,
    onAssign: (lineIndex: Int, guestIndex: Int, quantity: Int) -> Unit,
    formatAmount: (Long) -> String,
    quantityLabel: (Int) -> String,
    unassignedUnitsLabel: (Int) -> String,
    totalLabel: String,
    assignedLabel: String,
    unassignedLabel: String,
    modifier: Modifier = Modifier,
    guestLabel: (Int) -> String = { "Guest ${it + 1}" },
) {
    val spacing = DsTheme.spacing
    val guestCount = guests.size
    val subtotals = dsSplitItemGuestSubtotals(lines, guestCount)
    val unassigned = dsSplitItemUnassigned(lines, guestCount)
    val selected = selectedGuest.takeIf { it in guests.indices }
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(spacing.sm),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(spacing.sm),
        ) {
            guests.forEachIndexed { index, guest ->
                GuestTile(
                    label = guest.label ?: guestLabel(index),
                    subtotal = formatAmount(subtotals[index]),
                    selected = index == selected,
                    onClick = { onSelectGuest(index) },
                )
            }
        }
        lines.forEachIndexed { lineIndex, line ->
            if (lineIndex > 0) DsDivider()
            SplitItemRow(
                line = line,
                guestCount = guestCount,
                selectedGuest = selected,
                onAssign = { guestIndex, quantity -> onAssign(lineIndex, guestIndex, quantity) },
                formatAmount = formatAmount,
                quantityLabel = quantityLabel,
                unassignedUnitsLabel = unassignedUnitsLabel,
            )
        }
        DsTotalsRow(
            totalLabel,
            formatAmount(dsSplitItemTotal(lines)),
            modifier = Modifier.semantics(mergeDescendants = true) {},
            emphasis = DsTotalsEmphasis.Total,
        )
        DsTotalsRow(
            assignedLabel,
            formatAmount(subtotals.sum()),
            modifier = Modifier.semantics(mergeDescendants = true) {},
        )
        if (unassigned > 0L) {
            DsTotalsRow(
                unassignedLabel,
                formatAmount(unassigned),
                modifier = Modifier.semantics(mergeDescendants = true) {},
            )
        }
    }
}

@Composable
private fun GuestTile(
    label: String,
    subtotal: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val c = DsTheme.colors
    val shape = DsTheme.shapes.md
    Row(
        modifier =
            Modifier
                .heightIn(min = DsTheme.spacing.minTouchTarget)
                .clip(shape)
                .background(if (selected) c.primaryContainer else c.surface)
                .border(DsStroke.hairline, if (selected) c.primary else c.border, shape)
                .selectable(selected = selected, role = Role.RadioButton, onClick = onClick)
                .padding(horizontal = DsTheme.spacing.smd, vertical = DsTheme.spacing.xs),
        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (selected) {
            Icon(DsIcons.Check, contentDescription = null, tint = c.primary, modifier = Modifier.size(DsTheme.spacing.md))
        }
        Column {
            Text(label, style = DsTheme.typography.labelStrong, color = if (selected) c.primary else c.textPrimary, maxLines = 1)
            Text(subtotal, style = DsTheme.typography.caption, color = c.textSecondary, maxLines = 1)
        }
    }
}

@Composable
private fun SplitItemRow(
    line: DsSplitItemLine,
    guestCount: Int,
    selectedGuest: Int?,
    onAssign: (guestIndex: Int, quantity: Int) -> Unit,
    formatAmount: (Long) -> String,
    quantityLabel: (Int) -> String,
    unassignedUnitsLabel: (Int) -> String,
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val free = dsSplitItemFreeUnits(line, guestCount)
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
                    .semantics(mergeDescendants = true) {},
            verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
        ) {
            Text(line.name, style = t.bodyStrong, color = c.textPrimary)
            Row(horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                Text(quantityLabel(line.quantity), style = t.label, color = c.textSecondary)
                Text(formatAmount(line.totalMinor.coerceAtLeast(0L)), style = t.label, color = c.textSecondary)
            }
            Text(unassignedUnitsLabel(free), style = t.caption, color = if (free > 0) c.warning else c.textSecondary)
        }
        if (selectedGuest != null) {
            val held = dsSplitItemAssigned(line, guestCount)[selectedGuest]
            val max = held + free
            Box(Modifier.heightIn(min = DsTheme.spacing.minTouchTarget), contentAlignment = Alignment.Center) {
                DsQuantityStepper(
                    quantity = held,
                    onQuantityChange = { onAssign(selectedGuest, it.coerceIn(0, max)) },
                    min = 0,
                    max = max,
                )
            }
        }
    }
}

private fun previewAmount(minor: Long): String = "${minor / 100},${(minor % 100).toString().padStart(2, '0')} DA"

private val previewGuests = listOf(DsSplitGuest("Karim"), DsSplitGuest("Sonia"), DsSplitGuest())

@Composable
private fun PreviewSplitByItem(
    lines: List<DsSplitItemLine>,
    selectedGuest: Int = 0,
    guests: List<DsSplitGuest> = previewGuests,
) = DsSplitByItem(
    lines = lines,
    guests = guests,
    selectedGuest = selectedGuest,
    onSelectGuest = {},
    onAssign = { _, _, _ -> },
    formatAmount = ::previewAmount,
    quantityLabel = { "×$it" },
    unassignedUnitsLabel = { "$it restant(s)" },
    totalLabel = "Total",
    assignedLabel = "Réparti",
    unassignedLabel = "Non réparti",
)

@DsComponentPreview
@Composable
private fun DsSplitByItemNothingAssignedPreview() =
    DsPreview {
        PreviewSplitByItem(
            listOf(
                DsSplitItemLine("Cappuccino", quantity = 2, totalMinor = 40_000L),
                DsSplitItemLine("Croissant", quantity = 3, totalMinor = 27_000L),
            ),
        )
    }

@DsComponentPreview
@Composable
private fun DsSplitByItemPartlyAssignedPreview() =
    DsPreview {
        PreviewSplitByItem(
            listOf(
                DsSplitItemLine("Cappuccino", quantity = 2, totalMinor = 40_000L, assignedQuantities = listOf(1, 0, 0)),
                DsSplitItemLine("Croissant", quantity = 3, totalMinor = 27_000L, assignedQuantities = listOf(0, 1, 1)),
            ),
            selectedGuest = 1,
        )
    }

@DsComponentPreview
@Composable
private fun DsSplitByItemFullyAssignedPreview() =
    DsPreview {
        PreviewSplitByItem(
            listOf(
                DsSplitItemLine("Cappuccino", quantity = 2, totalMinor = 40_000L, assignedQuantities = listOf(1, 1, 0)),
                DsSplitItemLine("Croissant", quantity = 3, totalMinor = 27_000L, assignedQuantities = listOf(1, 1, 1)),
            ),
            selectedGuest = -1,
        )
    }

@DsComponentPreview
@Composable
private fun DsSplitByItemSharedLinePreview() =
    DsPreview {
        PreviewSplitByItem(
            listOf(DsSplitItemLine("Mezzé à partager", quantity = 3, totalMinor = 1_000L, assignedQuantities = listOf(2, 1))),
            guests = previewGuests.take(2),
        )
        Spacer(Modifier.height(DsTheme.spacing.lg))
        PreviewSplitByItem(
            listOf(DsSplitItemLine("Mezzé à partager", quantity = 3, totalMinor = 1_000L, assignedQuantities = listOf(2, 1))),
            selectedGuest = 1,
            guests = previewGuests.take(2),
        )
    }
