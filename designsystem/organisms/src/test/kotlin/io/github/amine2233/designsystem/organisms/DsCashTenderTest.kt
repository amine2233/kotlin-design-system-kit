package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DsCashTenderTest {
    @Test
    fun `the reference amount offers exact then the next bills`() {
        assertEquals(listOf(118_800L, 120_000L, 150_000L, 200_000L), dsQuickTenders(118_800))
    }

    @Test
    fun `an amount already on a step rounds strictly above it`() {
        assertEquals(listOf(100_000L, 110_000L, 150_000L, 200_000L), dsQuickTenders(100_000))
    }

    @Test
    fun `an amount just below a step rounds to that step`() {
        assertEquals(listOf(99_999L, 100_000L, 200_000L), dsQuickTenders(99_999))
    }

    @Test
    fun `an amount just above a step rounds to the next one`() {
        assertEquals(listOf(100_001L, 110_000L, 150_000L, 200_000L), dsQuickTenders(100_001))
    }

    @Test
    fun `zero due offers only the exact bill`() {
        assertEquals(listOf(0L), dsQuickTenders(0))
    }

    @Test
    fun `an amount near Long MAX_VALUE skips the steps that would overflow`() {
        val due = Long.MAX_VALUE - 15_000
        assertEquals(listOf(due, (due / 10_000 + 1) * 10_000), dsQuickTenders(due))
        assertEquals(listOf(Long.MAX_VALUE), dsQuickTenders(Long.MAX_VALUE))
    }

    @Test
    fun `bills are ascending, distinct and capped at maxRounded`() {
        val bills = dsQuickTenders(118_800, steps = listOf(200_000L, 10_000L, 100_000L, 50_000L, 100_000L), maxRounded = 2)
        assertEquals(listOf(118_800L, 120_000L, 150_000L), bills)
    }

    @Test
    fun `non-positive steps are ignored`() {
        assertEquals(listOf(118_800L, 150_000L), dsQuickTenders(118_800, steps = listOf(0L, -10_000L, 50_000L)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a negative due throws`() {
        dsQuickTenders(-1)
    }

    @Test
    fun `status follows the tendered amount`() {
        assertEquals(DsCashTenderStatus.Empty, dsCashTenderStatus(118_800, 0))
        assertEquals(DsCashTenderStatus.Change(0), dsCashTenderStatus(118_800, 118_800))
        assertEquals(DsCashTenderStatus.Change(31_200), dsCashTenderStatus(118_800, 150_000))
        assertEquals(DsCashTenderStatus.Missing(18_800), dsCashTenderStatus(118_800, 100_000))
        assertEquals(DsCashTenderStatus.Change(0), dsCashTenderStatus(0, 0))
    }

    @Test
    fun `only change is sufficient`() {
        assertTrue(DsCashTenderStatus.Change(0).isSufficient)
        assertTrue(DsCashTenderStatus.Change(31_200).isSufficient)
        assertFalse(DsCashTenderStatus.Missing(18_800).isSufficient)
        assertFalse(DsCashTenderStatus.Empty.isSufficient)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a negative due status throws`() {
        dsCashTenderStatus(-1, 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a negative tendered status throws`() {
        dsCashTenderStatus(0, -1)
    }

    @Test
    fun `digits append in minor units`() {
        assertEquals(150_000L, "150000".fold(0L) { amount, digit -> amount.applyKey(DsKey.Digit(digit)) })
    }

    @Test
    fun `backspace drops a digit and comma is a no-op`() {
        assertEquals(15_000L, 150_000L.applyKey(DsKey.Backspace))
        assertEquals(0L, 0L.applyKey(DsKey.Backspace))
        assertEquals(108_000L, 108_000L.applyKey(DsKey.Comma))
    }

    @Test
    fun `digits past MaxTendered are ignored`() {
        val max = DsCashTenderDefaults.MaxTendered
        assertEquals(max, max.applyKey(DsKey.Digit('0')))
        assertEquals(max, (max / 10).applyKey(DsKey.Digit('9')))
        assertEquals(max / 10 + 1, (max / 10 + 1).applyKey(DsKey.Digit('0')))
    }

    @Test
    fun `minor units format with grouping and two decimals`() {
        assertEquals("0,00", dsFormatMinorUnits(0))
        assertEquals("0,05", dsFormatMinorUnits(5))
        assertEquals("1 500,00", dsFormatMinorUnits(150_000))
        assertEquals("1 234 567,89", dsFormatMinorUnits(123_456_789))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `formatting a negative amount throws`() {
        dsFormatMinorUnits(-1)
    }
}
