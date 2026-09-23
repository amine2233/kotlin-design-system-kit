package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DsDateBoundsTest {
    private val feb28 = 1_772_236_800_000L
    private val mar06 = 1_772_755_200_000L
    private val day = 24 * 60 * 60 * 1000L

    @Test
    fun `no bounds allows everything`() {
        assertTrue(DsDateBounds().allows(feb28))
        assertTrue(DsDateBounds().allowsYear(1999))
    }

    @Test
    fun `bounds are inclusive on both ends`() {
        val bounds = DsDateBounds.between(feb28, mar06)
        assertTrue(bounds.allows(feb28))
        assertTrue(bounds.allows(mar06))
        assertFalse(bounds.allows(feb28 - day))
        assertFalse(bounds.allows(mar06 + day))
    }

    @Test
    fun `a day rule narrows the range further`() {
        val noWeekend = DsDateBounds.upTo(mar06).copy(isDayAllowed = { it != feb28 })
        assertFalse(noWeekend.allows(feb28))
        assertTrue(noWeekend.allows(feb28 - day))
    }

    @Test
    fun `years outside the bounds are refused`() {
        val bounds = DsDateBounds.upTo(feb28)
        assertTrue(bounds.allowsYear(2026))
        assertFalse(bounds.allowsYear(2027))
    }
}
