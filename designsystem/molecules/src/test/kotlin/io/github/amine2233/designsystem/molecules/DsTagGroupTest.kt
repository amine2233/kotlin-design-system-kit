package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test

class DsTagGroupTest {
    private val tags = listOf(DsTagEntry("a"), DsTagEntry("b"), DsTagEntry("c"))

    @Test
    fun `no max shows everything`() {
        assertEquals(tags to 0, dsVisibleTags(tags))
    }

    @Test
    fun `a max above the count hides nothing`() {
        assertEquals(tags to 0, dsVisibleTags(tags, max = 5))
        assertEquals(tags to 0, dsVisibleTags(tags, max = 3))
    }

    @Test
    fun `past the max the rest are counted, not dropped`() {
        assertEquals(tags.take(2) to 1, dsVisibleTags(tags, max = 2))
    }

    @Test
    fun `a max of zero or less counts them all as hidden`() {
        assertEquals(emptyList<DsTagEntry>() to 3, dsVisibleTags(tags, max = 0))
        assertEquals(emptyList<DsTagEntry>() to 3, dsVisibleTags(tags, max = -1))
    }

    @Test
    fun `an empty list stays empty`() {
        assertEquals(emptyList<DsTagEntry>() to 0, dsVisibleTags(emptyList(), max = 3))
    }
}
