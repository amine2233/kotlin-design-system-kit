package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test

class DsFilterGroupTest {
    @Test
    fun `single selection replaces whatever was selected`() {
        assertEquals(setOf(2), dsToggleFilter(setOf(0), 2, DsFilterSelection.Single))
        assertEquals(setOf(2), dsToggleFilter(emptySet(), 2, DsFilterSelection.Single))
    }

    @Test
    fun `single selection does not empty itself on a second tap`() {
        assertEquals(setOf(1), dsToggleFilter(setOf(1), 1, DsFilterSelection.Single))
    }

    @Test
    fun `multiple selection adds and removes`() {
        assertEquals(setOf(0, 1), dsToggleFilter(setOf(0), 1, DsFilterSelection.Multiple))
        assertEquals(setOf(0), dsToggleFilter(setOf(0, 1), 1, DsFilterSelection.Multiple))
    }

    @Test
    fun `multiple selection can be emptied by untapping the last one`() {
        assertEquals(emptySet<Int>(), dsToggleFilter(setOf(3), 3, DsFilterSelection.Multiple))
    }
}
