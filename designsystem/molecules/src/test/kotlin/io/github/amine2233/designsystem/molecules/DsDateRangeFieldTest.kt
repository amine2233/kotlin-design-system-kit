package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DsDateRangeFieldTest {
    private val feb28 = 1_772_236_800_000L
    private val mar06 = 1_772_755_200_000L

    @Test
    fun `an empty range has no label so the placeholder shows`() {
        assertNull(dsFormatDateRange(null, null))
    }

    @Test
    fun `a complete range is shown end to end`() {
        assertEquals("28/02/2026 → 06/03/2026", dsFormatDateRange(feb28, mar06))
    }

    @Test
    fun `a started range keeps the end open`() {
        assertEquals("28/02/2026 → …", dsFormatDateRange(feb28, null))
    }
}
