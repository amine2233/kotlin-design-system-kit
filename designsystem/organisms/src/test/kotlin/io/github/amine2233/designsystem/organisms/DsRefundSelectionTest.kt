package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DsRefundSelectionTest {
    private val coffee = DsRefundLine("Coffee", unitPriceMinor = 45_000, quantityBought = 5, quantityRefunded = 2)
    private val refunded = DsRefundLine("Tea", unitPriceMinor = 30_000, quantityBought = 2, quantityRefunded = 2)

    @Test
    fun `refundable quantity is bought minus refunded`() {
        assertEquals(3, dsRefundableQuantity(coffee))
        assertEquals(0, dsRefundableQuantity(refunded))
    }

    @Test
    fun `a selection within range passes through`() {
        assertEquals(0, dsClampRefundQuantity(coffee, 0))
        assertEquals(2, dsClampRefundQuantity(coffee, 2))
        assertEquals(3, dsClampRefundQuantity(coffee, 3))
    }

    @Test
    fun `a selection above the refundable quantity is capped`() {
        assertEquals(3, dsClampRefundQuantity(coffee, 7))
        assertEquals(0, dsClampRefundQuantity(refunded, 1))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a negative selection throws`() {
        dsClampRefundQuantity(coffee, -1)
    }

    @Test
    fun `line amount is unit price times the clamped quantity`() {
        assertEquals(90_000L, dsRefundLineAmount(coffee, 2))
        assertEquals(135_000L, dsRefundLineAmount(coffee, 7))
    }

    @Test
    fun `a fully refunded line amounts to zero`() {
        assertEquals(0L, dsRefundLineAmount(refunded, 2))
    }

    @Test(expected = ArithmeticException::class)
    fun `a line amount overflowing Long throws`() {
        dsRefundLineAmount(DsRefundLine("Gold", unitPriceMinor = Long.MAX_VALUE, quantityBought = 2), 2)
    }

    @Test
    fun `total sums the line amounts`() {
        val tea = DsRefundLine("Tea", unitPriceMinor = 30_000, quantityBought = 3)
        assertEquals(150_000L, dsRefundTotal(listOf(coffee, tea, refunded), listOf(2, 2, 1)))
    }

    @Test
    fun `total of empty lists is zero`() {
        assertEquals(0L, dsRefundTotal(emptyList(), emptyList()))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `total with mismatched sizes throws`() {
        dsRefundTotal(listOf(coffee), listOf(1, 2))
    }

    @Test(expected = ArithmeticException::class)
    fun `a total overflowing Long throws`() {
        val big = DsRefundLine("Gold", unitPriceMinor = Long.MAX_VALUE, quantityBought = 1)
        dsRefundTotal(listOf(big, big), listOf(1, 1))
    }

    @Test
    fun `has selection only once a priced unit is selected`() {
        val free = DsRefundLine("Water", unitPriceMinor = 0, quantityBought = 1)
        assertFalse(dsHasRefundSelection(listOf(coffee, free), listOf(0, 0)))
        assertFalse(dsHasRefundSelection(listOf(coffee, free), listOf(0, 1)))
        assertTrue(dsHasRefundSelection(listOf(coffee, free), listOf(1, 0)))
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a negative unit price throws`() {
        DsRefundLine("Coffee", unitPriceMinor = -1, quantityBought = 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a negative bought quantity throws`() {
        DsRefundLine("Coffee", unitPriceMinor = 100, quantityBought = -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `a negative refunded quantity throws`() {
        DsRefundLine("Coffee", unitPriceMinor = 100, quantityBought = 1, quantityRefunded = -1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `refunding more than bought throws`() {
        DsRefundLine("Coffee", unitPriceMinor = 100, quantityBought = 1, quantityRefunded = 2)
    }
}
