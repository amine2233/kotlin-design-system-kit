package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Test

class ApplyKeyTest {
    @Test
    fun `digits are typed right to left in cents with french grouping`() {
        var s = "0,00"
        "150000".forEach { s = s.applyKey(DsKey.Digit(it)) }
        assertEquals("1 500,00", s)
    }

    @Test
    fun `backspace drops the last digit and comma is a no-op`() {
        assertEquals("15,00", "150,00".applyKey(DsKey.Backspace))
        assertEquals("0,00", "0,00".applyKey(DsKey.Backspace))
        assertEquals("1 080,00", "1 080,00".applyKey(DsKey.Comma))
    }
}
