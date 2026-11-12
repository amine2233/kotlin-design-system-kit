package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test

class DsTipSelectorTest {
    private fun preset(percent: Int) = DsTipSelection.Preset(percent)

    @Test
    fun `an exact percentage needs no rounding`() {
        assertEquals(100L, dsTipAmount(1000L, preset(10)))
    }

    @Test
    fun `just under half rounds down`() {
        assertEquals(100L, dsTipAmount(1004L, preset(10)))
    }

    @Test
    fun `exactly half rounds up`() {
        assertEquals(101L, dsTipAmount(1005L, preset(10)))
    }

    @Test
    fun `just over half rounds up`() {
        assertEquals(101L, dsTipAmount(1006L, preset(10)))
    }

    @Test
    fun `a zero subtotal gives no tip`() {
        assertEquals(0L, dsTipAmount(0L, preset(15)))
        assertEquals(0L, dsTipTotal(0L, preset(15)))
    }

    @Test
    fun `zero percent gives no tip`() {
        assertEquals(0L, dsTipAmount(1000L, preset(0)))
    }

    @Test
    fun `none gives no tip`() {
        assertEquals(0L, dsTipAmount(1000L, DsTipSelection.None))
        assertEquals(1000L, dsTipTotal(1000L, DsTipSelection.None))
    }

    @Test
    fun `a custom amount is used as is`() {
        assertEquals(250L, dsTipAmount(1000L, DsTipSelection.Custom(250L)))
        assertEquals(1250L, dsTipTotal(1000L, DsTipSelection.Custom(250L)))
    }

    @Test
    fun `a negative subtotal counts as zero`() {
        assertEquals(0L, dsTipAmount(-1000L, preset(10)))
        assertEquals(0L, dsTipTotal(-1000L, preset(10)))
    }

    @Test
    fun `a negative percent counts as zero`() {
        assertEquals(0L, dsTipAmount(1000L, preset(-10)))
    }

    @Test
    fun `a negative custom amount counts as zero`() {
        assertEquals(0L, dsTipAmount(1000L, DsTipSelection.Custom(-250L)))
        assertEquals(1000L, dsTipTotal(1000L, DsTipSelection.Custom(-250L)))
    }

    @Test
    fun `a large subtotal does not overflow`() {
        assertEquals(1_800_000_000_000_000L, dsTipAmount(9_000_000_000_000_000L, preset(20)))
        assertEquals(Long.MAX_VALUE, dsTipAmount(Long.MAX_VALUE, preset(100)))
    }

    @Test
    fun `the new total adds the tip to the subtotal`() {
        assertEquals(108L, dsTipAmount(1080L, preset(10)))
        assertEquals(1188L, dsTipTotal(1080L, preset(10)))
    }
}
