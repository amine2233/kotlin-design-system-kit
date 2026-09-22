package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DsSearchableDropdownTest {
    @Test
    fun `matching ignores case and accents`() {
        assertTrue(dsMatchesQuery("Crème brûlée", "creme"))
        assertTrue(dsMatchesQuery("Crème brûlée", "BRULEE"))
        assertTrue(dsMatchesQuery("Thé vert", "the"))
    }

    @Test
    fun `an empty or blank query keeps every option`() {
        assertTrue(dsMatchesQuery("Espresso", ""))
        assertTrue(dsMatchesQuery("Espresso", "   "))
    }

    @Test
    fun `a query that matches nothing filters the option out`() {
        assertFalse(dsMatchesQuery("Espresso", "latte"))
    }
}
