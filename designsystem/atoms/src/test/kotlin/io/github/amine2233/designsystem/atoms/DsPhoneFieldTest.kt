package io.github.amine2233.designsystem.atoms

import org.junit.Assert.assertEquals
import org.junit.Test

class DsPhoneFieldTest {
    @Test
    fun `digits are grouped two by two`() {
        assertEquals("06 12 34 56 78", dsFormatPhone("0612345678"))
    }

    @Test
    fun `a partial number is grouped as far as it goes without a trailing space`() {
        assertEquals("06", dsFormatPhone("06"))
        assertEquals("06 1", dsFormatPhone("061"))
        assertEquals("", dsFormatPhone(""))
    }

    @Test
    fun `digits past the last group are kept`() {
        assertEquals("06 12 34 56 7890", dsFormatPhone("06123456" + "7890"))
    }

    @Test
    fun `another grouping is honoured`() {
        assertEquals("021 45 67 89", dsFormatPhone("021456789", listOf(3, 2, 2, 2)))
    }
}
