package io.github.amine2233.designsystem.molecules

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import org.junit.Assert.assertEquals
import org.junit.Test

class DsPopoverTest {
    private val window = IntSize(400, 800)
    private val popover = IntSize(200, 100)
    private val gap = 10

    private fun offset(
        anchor: IntRect,
        placement: DsPopoverPlacement,
        size: IntSize = popover,
        direction: LayoutDirection = LayoutDirection.Ltr,
    ) = dsPopoverOffset(anchor, window, size, placement, direction, gap)

    @Test
    fun `below sits under the anchor when it fits`() {
        assertEquals(IntOffset(50, 360), offset(IntRect(50, 300, 150, 350), DsPopoverPlacement.Below))
    }

    @Test
    fun `above sits over the anchor when it fits`() {
        assertEquals(IntOffset(50, 190), offset(IntRect(50, 300, 150, 350), DsPopoverPlacement.Above))
    }

    @Test
    fun `below flips above when there is no room under the anchor`() {
        assertEquals(IntOffset(50, 640), offset(IntRect(50, 750, 150, 790), DsPopoverPlacement.Below))
    }

    @Test
    fun `above flips below when there is no room over the anchor`() {
        assertEquals(IntOffset(50, 70), offset(IntRect(50, 10, 150, 60), DsPopoverPlacement.Above))
    }

    @Test
    fun `no room on either side picks the roomier side and clamps it into the window`() {
        val tall = IntSize(200, 500)
        assertEquals(IntOffset(50, 300), offset(IntRect(50, 300, 150, 350), DsPopoverPlacement.Above, tall))
        assertEquals(IntOffset(50, 0), offset(IntRect(50, 450, 150, 500), DsPopoverPlacement.Below, tall))
    }

    @Test
    fun `taller than the window pins to the top`() {
        assertEquals(IntOffset(50, 0), offset(IntRect(50, 300, 150, 350), DsPopoverPlacement.Below, IntSize(200, 900)))
    }

    @Test
    fun `clamps at the end edge of the window`() {
        assertEquals(IntOffset(200, 360), offset(IntRect(300, 300, 380, 350), DsPopoverPlacement.Below))
    }

    @Test
    fun `rtl aligns to the anchor's right edge and clamps at the left edge`() {
        assertEquals(
            IntOffset(150, 360),
            offset(IntRect(250, 300, 350, 350), DsPopoverPlacement.Below, direction = LayoutDirection.Rtl),
        )
        assertEquals(
            IntOffset(0, 360),
            offset(IntRect(20, 300, 120, 350), DsPopoverPlacement.Below, direction = LayoutDirection.Rtl),
        )
    }
}
