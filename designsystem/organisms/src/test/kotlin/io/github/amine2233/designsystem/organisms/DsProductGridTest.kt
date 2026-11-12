package io.github.amine2233.designsystem.organisms

import io.github.amine2233.designsystem.core.DsWindowSize
import org.junit.Assert.assertEquals
import org.junit.Test

class DsProductGridTest {
    @Test
    fun `a compact window shows two columns`() {
        assertEquals(2, dsProductGridColumns(DsWindowSize.Compact))
    }

    @Test
    fun `an expanded window shows four columns`() {
        assertEquals(4, dsProductGridColumns(DsWindowSize.Expanded))
    }
}
