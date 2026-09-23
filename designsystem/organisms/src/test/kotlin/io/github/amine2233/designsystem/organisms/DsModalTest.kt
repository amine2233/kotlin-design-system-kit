package io.github.amine2233.designsystem.organisms

import io.github.amine2233.designsystem.core.DsWindowSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DsModalTest {
    @Test
    fun `full fills nearly everything on both sizes`() {
        assertEquals(0.95f, dsModalHeightFraction(DsModalSize.Full, DsWindowSize.Compact))
        assertEquals(0.9f, dsModalHeightFraction(DsModalSize.Full, DsWindowSize.Expanded))
    }

    @Test
    fun `half leaves the phone sheet room for its header`() {
        assertEquals(0.55f, dsModalHeightFraction(DsModalSize.Half, DsWindowSize.Compact))
        assertEquals(0.5f, dsModalHeightFraction(DsModalSize.Half, DsWindowSize.Expanded))
    }

    @Test
    fun `half is always shorter than full`() {
        DsWindowSize.entries.forEach { window ->
            assertTrue(
                dsModalHeightFraction(DsModalSize.Half, window) < dsModalHeightFraction(DsModalSize.Full, window),
            )
        }
    }
}
