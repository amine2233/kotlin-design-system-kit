package io.github.amine2233.designsystem.atoms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Locale

class DsProgressRingTest {
    @Test
    fun `progress inside the range is kept`() {
        assertEquals(0.42f, dsProgressRingFraction(0.42f))
    }

    @Test
    fun `progress outside the range is clamped`() {
        assertEquals(0f, dsProgressRingFraction(-0.3f))
        assertEquals(1f, dsProgressRingFraction(1.7f))
    }

    @Test
    fun `non-finite progress reads as empty`() {
        assertEquals(0f, dsProgressRingFraction(Float.NaN))
        assertEquals(0f, dsProgressRingFraction(Float.POSITIVE_INFINITY))
        assertEquals(0f, dsProgressRingFraction(Float.NEGATIVE_INFINITY))
    }

    @Test
    fun `percent text is a whole number`() {
        assertEquals("0%", dsProgressRingPercentText(0f, Locale.US))
        assertEquals("70%", dsProgressRingPercentText(0.7f, Locale.US))
        assertEquals("100%", dsProgressRingPercentText(1f, Locale.US))
        assertEquals("0%", dsProgressRingPercentText(0.004f, Locale.US))
    }

    @Test
    fun `percent text follows the locale`() {
        val text = dsProgressRingPercentText(0.7f, Locale.FRANCE)
        assertTrue(text, text.startsWith("70"))
        assertTrue(text, text.endsWith("%"))
    }
}
