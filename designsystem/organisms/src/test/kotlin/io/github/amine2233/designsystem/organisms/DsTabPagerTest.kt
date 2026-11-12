package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DsTabPagerTest {
    @Test
    fun `an index inside the pages is kept`() {
        assertEquals(0, dsTabPagerPage(0, pageCount = 3))
        assertEquals(2, dsTabPagerPage(2, pageCount = 3))
    }

    @Test
    fun `a negative index becomes the first page`() {
        assertEquals(0, dsTabPagerPage(-1, pageCount = 3))
    }

    @Test
    fun `an index past the end becomes the last page`() {
        assertEquals(2, dsTabPagerPage(7, pageCount = 3))
    }

    @Test
    fun `no pages gives page zero`() {
        assertEquals(0, dsTabPagerPage(0, pageCount = 0))
        assertEquals(0, dsTabPagerPage(4, pageCount = 0))
    }

    @Test
    fun `settling on another page reports it`() {
        assertEquals(2, dsTabPagerSettledSelection(settledPage = 2, selectedIndex = 0, userDragged = true))
    }

    @Test
    fun `settling on the selected page reports nothing`() {
        assertNull(dsTabPagerSettledSelection(settledPage = 1, selectedIndex = 1, userDragged = true))
    }

    @Test
    fun `settling on the clamped page of an out-of-range index reports nothing`() {
        assertNull(dsTabPagerSettledSelection(settledPage = 2, selectedIndex = dsTabPagerPage(7, pageCount = 3), userDragged = true))
        assertNull(dsTabPagerSettledSelection(settledPage = 0, selectedIndex = dsTabPagerPage(-1, pageCount = 3), userDragged = true))
    }

    @Test
    fun `settling on an intermediate page after a programmatic move reports nothing`() {
        assertNull(dsTabPagerSettledSelection(settledPage = 1, selectedIndex = 2, userDragged = false))
    }
}
