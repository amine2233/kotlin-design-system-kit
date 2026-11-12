package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Test

class DsStaffPinLoginTest {
    @Test
    fun `digits append`() {
        assertEquals("12", dsApplyPinKey("1", DsKey.Digit('2')))
    }

    @Test
    fun `digits past the length are ignored`() {
        assertEquals("1234", dsApplyPinKey("1234", DsKey.Digit('5')))
    }

    @Test
    fun `backspace removes one digit`() {
        assertEquals("12", dsApplyPinKey("123", DsKey.Backspace))
    }

    @Test
    fun `backspace on an empty pin stays empty`() {
        assertEquals("", dsApplyPinKey("", DsKey.Backspace))
    }

    @Test
    fun `comma is a no-op`() {
        assertEquals("12", dsApplyPinKey("12", DsKey.Comma))
    }

    @Test
    fun `a custom length is respected`() {
        assertEquals("12345", dsApplyPinKey("1234", DsKey.Digit('5'), length = 6))
        assertEquals("123456", dsApplyPinKey("12345", DsKey.Digit('6'), length = 6))
        assertEquals("123456", dsApplyPinKey("123456", DsKey.Digit('7'), length = 6))
    }
}
