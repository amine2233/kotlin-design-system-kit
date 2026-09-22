package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test

class DsOtpFieldTest {
    @Test
    fun `non digits are dropped and the code never exceeds the length`() {
        assertEquals("4218", otpDigits("4a2-1 8", 6))
        assertEquals("123456", otpDigits("12345678", 6))
        assertEquals("", otpDigits("code", 6))
    }

    @Test
    fun `a pasted code keeps its leading zeros`() {
        assertEquals("004218", otpDigits("004218", 6))
    }
}
