package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Test

class DsSplitBillTest {
    @Test
    fun `an uneven total gives the remainder to the first parts`() {
        val shares = dsEqualSplit(1000L, 3)
        assertEquals(listOf(334L, 333L, 333L), shares)
        assertEquals(1000L, shares.sum())
    }

    @Test
    fun `an even total splits into identical shares`() {
        assertEquals(listOf(250L, 250L, 250L, 250L), dsEqualSplit(1000L, 4))
    }

    @Test
    fun `a zero total gives zero shares`() {
        assertEquals(listOf(0L, 0L, 0L, 0L), dsEqualSplit(0L, 4))
    }

    @Test
    fun `a total smaller than the part count gives one unit to the first parts`() {
        assertEquals(listOf(1L, 0L, 0L), dsEqualSplit(1L, 3))
    }

    @Test
    fun `a negative total counts as zero`() {
        assertEquals(listOf(0L, 0L), dsEqualSplit(-500L, 2))
    }

    @Test
    fun `no parts gives an empty split`() {
        assertEquals(emptyList<Long>(), dsEqualSplit(1000L, 0))
        assertEquals(emptyList<Long>(), dsEqualSplit(1000L, -2))
    }

    @Test
    fun `equal mode ignores custom amounts`() {
        val parts = listOf(DsSplitPart(amountMinor = 900L), DsSplitPart(amountMinor = 5L), DsSplitPart())
        assertEquals(listOf(334L, 333L, 333L), dsSplitAmounts(1000L, DsSplitMode.Equal, parts))
    }

    @Test
    fun `custom mode uses each amount and clamps negatives`() {
        val parts = listOf(DsSplitPart(amountMinor = 600L), DsSplitPart(amountMinor = -50L), DsSplitPart(amountMinor = 300L))
        assertEquals(listOf(600L, 0L, 300L), dsSplitAmounts(1000L, DsSplitMode.Custom, parts))
    }

    @Test
    fun `amounts below the total leave an unallocated difference`() {
        assertEquals(100L, dsSplitUnallocated(1000L, listOf(600L, 300L)))
    }

    @Test
    fun `amounts matching the total are balanced`() {
        assertEquals(0L, dsSplitUnallocated(1000L, listOf(600L, 400L)))
    }

    @Test
    fun `amounts above the total are over-allocated`() {
        assertEquals(-150L, dsSplitUnallocated(1000L, listOf(700L, 450L)))
    }

    @Test
    fun `unallocated treats negative inputs as zero`() {
        assertEquals(1000L, dsSplitUnallocated(1000L, listOf(-200L)))
        assertEquals(-300L, dsSplitUnallocated(-100L, listOf(300L)))
    }

    @Test
    fun `nothing paid sums to zero`() {
        assertEquals(0L, dsSplitPaid(listOf(334L, 333L, 333L), List(3) { DsSplitPart() }))
    }

    @Test
    fun `some paid parts sum their amounts`() {
        val parts = listOf(DsSplitPart(paid = true), DsSplitPart(), DsSplitPart(paid = true))
        assertEquals(667L, dsSplitPaid(listOf(334L, 333L, 333L), parts))
    }

    @Test
    fun `all paid parts sum to the total`() {
        assertEquals(1000L, dsSplitPaid(listOf(334L, 333L, 333L), List(3) { DsSplitPart(paid = true) }))
    }
}
