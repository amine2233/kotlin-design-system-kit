package io.github.amine2233.designsystem.molecules

import io.github.amine2233.designsystem.molecules.DsDiscountMode.FixedAmount
import io.github.amine2233.designsystem.molecules.DsDiscountMode.Percent
import org.junit.Assert.assertEquals
import org.junit.Test

class DsDiscountTest {
    @Test
    fun `a typical percent takes its share of the subtotal`() {
        assertEquals(DsDiscountResult(10800, 97200), dsDiscount(108000, Percent, 1000))
    }

    @Test
    fun `a typical fixed amount is taken off as is`() {
        assertEquals(DsDiscountResult(50000, 58000), dsDiscount(108000, FixedAmount, 50000))
    }

    @Test
    fun `exactly half a minor unit rounds up`() {
        assertEquals(DsDiscountResult(1, 4), dsDiscount(5, Percent, 1000))
    }

    @Test
    fun `less than half a minor unit rounds down`() {
        assertEquals(DsDiscountResult(125, 876), dsDiscount(1001, Percent, 1250))
    }

    @Test
    fun `zero percent takes nothing`() {
        assertEquals(DsDiscountResult(0, 108000), dsDiscount(108000, Percent, 0))
    }

    @Test
    fun `a hundred percent takes the whole subtotal`() {
        assertEquals(DsDiscountResult(108000, 0), dsDiscount(108000, Percent, 10000))
    }

    @Test
    fun `over a hundred percent is clamped to the subtotal`() {
        assertEquals(DsDiscountResult(108000, 0), dsDiscount(108000, Percent, 15000))
    }

    @Test
    fun `a fixed amount above the subtotal is clamped to the subtotal`() {
        assertEquals(DsDiscountResult(108000, 0), dsDiscount(108000, FixedAmount, 200000))
    }

    @Test
    fun `a zero subtotal has no discount`() {
        assertEquals(DsDiscountResult(0, 0), dsDiscount(0, Percent, 1000))
    }

    @Test
    fun `a negative value counts as zero`() {
        assertEquals(DsDiscountResult(0, 108000), dsDiscount(108000, FixedAmount, -500))
    }

    @Test
    fun `a negative subtotal counts as zero`() {
        assertEquals(DsDiscountResult(0, 0), dsDiscount(-100, Percent, 1000))
    }

    @Test
    fun `a subtotal above Int range is computed exactly`() {
        assertEquals(DsDiscountResult(500_000_000, 4_500_000_000), dsDiscount(5_000_000_000, Percent, 1000))
    }

    @Test
    fun `the largest subtotal does not overflow`() {
        assertEquals(DsDiscountResult(Long.MAX_VALUE, 0), dsDiscount(Long.MAX_VALUE, Percent, 10000))
    }
}
