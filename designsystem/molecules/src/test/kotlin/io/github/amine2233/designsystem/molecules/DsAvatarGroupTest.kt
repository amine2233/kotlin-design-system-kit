package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test

class DsAvatarGroupTest {
    @Test
    fun `fewer avatars than the max are all shown`() {
        assertEquals(2 to 0, dsVisibleAvatars(count = 2, maxVisible = 3))
    }

    @Test
    fun `exactly the max hides nothing`() {
        assertEquals(3 to 0, dsVisibleAvatars(count = 3, maxVisible = 3))
    }

    @Test
    fun `past the max the rest are counted, not dropped`() {
        assertEquals(3 to 4, dsVisibleAvatars(count = 7, maxVisible = 3))
    }

    @Test
    fun `a max of zero or less still shows one avatar`() {
        assertEquals(1 to 4, dsVisibleAvatars(count = 5, maxVisible = 0))
        assertEquals(1 to 4, dsVisibleAvatars(count = 5, maxVisible = -2))
    }

    @Test
    fun `no avatars shows nothing`() {
        assertEquals(0 to 0, dsVisibleAvatars(count = 0, maxVisible = 3))
    }

    @Test
    fun `names without hidden people are joined with et`() {
        assertEquals("", dsAvatarGroupDescription(emptyList(), hidden = 0))
        assertEquals("Amine", dsAvatarGroupDescription(listOf("Amine"), hidden = 0))
        assertEquals("Amine et Bob", dsAvatarGroupDescription(listOf("Amine", "Bob"), hidden = 0))
        assertEquals("Amine, Bob et Chloé", dsAvatarGroupDescription(listOf("Amine", "Bob", "Chloé"), hidden = 0))
    }

    @Test
    fun `one hidden person is singular`() {
        assertEquals("Amine, Bob et 1 autre", dsAvatarGroupDescription(listOf("Amine", "Bob"), hidden = 1))
    }

    @Test
    fun `several hidden people are plural`() {
        assertEquals(
            "Amine, Bob, Chloé et 2 autres",
            dsAvatarGroupDescription(listOf("Amine", "Bob", "Chloé"), hidden = 2),
        )
    }
}
