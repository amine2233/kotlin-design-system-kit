package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test

class DsStatisticTileTest {
    @Test
    fun `when up is good, up is positive and down is negative`() {
        assertEquals(DsTrendTone.Positive, dsTrendTone(DsTrendDirection.Up, upIsGood = true))
        assertEquals(DsTrendTone.Negative, dsTrendTone(DsTrendDirection.Down, upIsGood = true))
    }

    @Test
    fun `when up is bad, up is negative and down is positive`() {
        assertEquals(DsTrendTone.Negative, dsTrendTone(DsTrendDirection.Up, upIsGood = false))
        assertEquals(DsTrendTone.Positive, dsTrendTone(DsTrendDirection.Down, upIsGood = false))
    }

    @Test
    fun `flat is always neutral`() {
        assertEquals(DsTrendTone.Neutral, dsTrendTone(DsTrendDirection.Flat, upIsGood = true))
        assertEquals(DsTrendTone.Neutral, dsTrendTone(DsTrendDirection.Flat, upIsGood = false))
    }

    @Test
    fun `without a trend the description is label and value`() {
        assertEquals("Ventes, 1 250 DA", dsStatisticTileDescription("Ventes", "1 250 DA", trend = null))
    }

    @Test
    fun `an upward trend reads en hausse with the delta`() {
        assertEquals(
            "Ventes, 1 250 DA, en hausse +12 %",
            dsStatisticTileDescription("Ventes", "1 250 DA", DsStatisticTrend(DsTrendDirection.Up, "+12 %")),
        )
    }

    @Test
    fun `a downward trend reads en baisse with the delta`() {
        assertEquals(
            "Clients, 36, en baisse −4 %",
            dsStatisticTileDescription("Clients", "36", DsStatisticTrend(DsTrendDirection.Down, "−4 %")),
        )
    }

    @Test
    fun `a flat trend reads stable with the delta`() {
        assertEquals(
            "Panier moyen, 820 DA, stable 0 %",
            dsStatisticTileDescription("Panier moyen", "820 DA", DsStatisticTrend(DsTrendDirection.Flat, "0 %")),
        )
    }
}
