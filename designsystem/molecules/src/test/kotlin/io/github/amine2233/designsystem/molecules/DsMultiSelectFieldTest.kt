package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DsMultiSelectFieldTest {
    @Test
    fun `an empty selection has no summary so the placeholder shows`() {
        assertNull(dsSummarizeSelection(emptyList()))
    }

    @Test
    fun `a short selection is listed and a long one is counted`() {
        assertEquals("Gluten, Lait", dsSummarizeSelection(listOf("Gluten", "Lait")))
        assertEquals("3 sélectionnés", dsSummarizeSelection(listOf("Gluten", "Lait", "Soja")))
    }
}
