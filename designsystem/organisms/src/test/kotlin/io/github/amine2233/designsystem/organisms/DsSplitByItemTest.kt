package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Test

class DsSplitByItemTest {
    @Test
    fun `an uneven line split two plus one gives the remainder units to the first guest`() {
        val line = DsSplitItemLine("Mezzé", quantity = 3, totalMinor = 1000L, assignedQuantities = listOf(2, 1))
        val shares = dsSplitItemShares(line, 2)
        assertEquals(listOf(667L, 333L), shares)
        assertEquals(1000L, shares.sum())
    }

    @Test
    fun `a partly assigned line charges only the assigned units`() {
        val line = DsSplitItemLine("Croissant", quantity = 3, totalMinor = 1000L, assignedQuantities = listOf(0, 1))
        assertEquals(listOf(0L, 334L), dsSplitItemShares(line, 2))
        assertEquals(2, dsSplitItemFreeUnits(line, 2))
        assertEquals(666L, dsSplitItemUnassigned(listOf(line), 2))
    }

    @Test
    fun `a line with nothing assigned leaves its whole total unassigned`() {
        val line = DsSplitItemLine("Cappuccino", quantity = 2, totalMinor = 40_000L)
        assertEquals(listOf(0L, 0L, 0L), dsSplitItemShares(line, 3))
        assertEquals(2, dsSplitItemFreeUnits(line, 3))
        assertEquals(40_000L, dsSplitItemUnassigned(listOf(line), 3))
    }

    @Test
    fun `a zero total gives zero shares`() {
        val line = DsSplitItemLine("Eau", quantity = 2, totalMinor = 0L, assignedQuantities = listOf(1, 1))
        assertEquals(listOf(0L, 0L), dsSplitItemShares(line, 2))
        assertEquals(0L, dsSplitItemUnassigned(listOf(line), 2))
    }

    @Test
    fun `a zero quantity gives no share and counts its total as unassigned`() {
        val line = DsSplitItemLine("Service", quantity = 0, totalMinor = 500L, assignedQuantities = listOf(1, 1))
        assertEquals(listOf(0, 0), dsSplitItemAssigned(line, 2))
        assertEquals(listOf(0L, 0L), dsSplitItemShares(line, 2))
        assertEquals(0, dsSplitItemFreeUnits(line, 2))
        assertEquals(500L, dsSplitItemUnassigned(listOf(line), 2))
    }

    @Test
    fun `over-assigned input is cut back from the last guests`() {
        val line = DsSplitItemLine("Thé", quantity = 2, totalMinor = 300L, assignedQuantities = listOf(2, 1))
        assertEquals(listOf(2, 0), dsSplitItemAssigned(line, 2))
        assertEquals(listOf(300L, 0L), dsSplitItemShares(line, 2))
        assertEquals(0, dsSplitItemFreeUnits(line, 2))
        assertEquals(0L, dsSplitItemUnassigned(listOf(line), 2))
    }

    @Test
    fun `a negative assigned value counts as zero`() {
        val line = DsSplitItemLine("Jus", quantity = 2, totalMinor = 400L, assignedQuantities = listOf(-3, 1))
        assertEquals(listOf(0, 1), dsSplitItemAssigned(line, 2))
        assertEquals(listOf(0L, 200L), dsSplitItemShares(line, 2))
        assertEquals(1, dsSplitItemFreeUnits(line, 2))
    }

    @Test
    fun `missing entries count as zero and extra entries are ignored`() {
        val line = DsSplitItemLine("Jus", quantity = 3, totalMinor = 300L, assignedQuantities = listOf(1, 0, 2))
        assertEquals(listOf(1), dsSplitItemAssigned(line, 1))
        assertEquals(listOf(1, 0, 0), dsSplitItemAssigned(line.copy(assignedQuantities = listOf(1)), 3))
    }

    @Test
    fun `guest subtotals plus the unassigned amount equal the bill total`() {
        val lines =
            listOf(
                DsSplitItemLine("Cappuccino", quantity = 2, totalMinor = 40_000L, assignedQuantities = listOf(1, 1)),
                DsSplitItemLine("Mezzé", quantity = 3, totalMinor = 1000L, assignedQuantities = listOf(1, 1)),
                DsSplitItemLine("Croissant", quantity = 1, totalMinor = 9_000L, assignedQuantities = listOf(0, 1)),
                DsSplitItemLine("Service", quantity = 0, totalMinor = 500L),
                DsSplitItemLine("Avoir", quantity = 1, totalMinor = -200L, assignedQuantities = listOf(1)),
            )
        val subtotals = dsSplitItemGuestSubtotals(lines, 2)
        assertEquals(listOf(20_334L, 29_333L), subtotals)
        assertEquals(50_500L, dsSplitItemTotal(lines))
        assertEquals(833L, dsSplitItemUnassigned(lines, 2))
        assertEquals(dsSplitItemTotal(lines), subtotals.sum() + dsSplitItemUnassigned(lines, 2))
    }
}
