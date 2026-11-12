package io.github.amine2233.designsystem.atoms

import io.github.amine2233.designsystem.atoms.DsStarFill.Empty
import io.github.amine2233.designsystem.atoms.DsStarFill.Full
import io.github.amine2233.designsystem.atoms.DsStarFill.Half
import org.junit.Assert.assertEquals
import org.junit.Test

class DsRatingTest {
    @Test
    fun `values outside the range are clamped`() {
        assertEquals(0f, dsRatingClamp(-1f, 5, 0.5f))
        assertEquals(5f, dsRatingClamp(9f, 5, 0.5f))
    }

    @Test
    fun `NaN and infinities count as zero`() {
        assertEquals(0f, dsRatingClamp(Float.NaN, 5, 0.5f))
        assertEquals(0f, dsRatingClamp(Float.POSITIVE_INFINITY, 5, 0.5f))
        assertEquals(0f, dsRatingClamp(Float.NEGATIVE_INFINITY, 5, 0.5f))
    }

    @Test
    fun `half steps round ties up`() {
        assertEquals(0.5f, dsRatingClamp(0.25f, 5, 0.5f))
        assertEquals(1f, dsRatingClamp(0.75f, 5, 0.5f))
        assertEquals(0f, dsRatingClamp(0.24f, 5, 0.5f))
    }

    @Test
    fun `whole steps round ties up`() {
        assertEquals(0f, dsRatingClamp(0.25f, 5, 1f))
        assertEquals(1f, dsRatingClamp(0.75f, 5, 1f))
        assertEquals(3f, dsRatingClamp(2.5f, 5, 1f))
    }

    @Test
    fun `a max below one counts as one`() {
        assertEquals(1f, dsRatingClamp(3f, 1, 0.5f))
        assertEquals(1f, dsRatingClamp(3f, 0, 0.5f))
        assertEquals(1f, dsRatingClamp(3f, -2, 0.5f))
    }

    @Test
    fun `stars split into full half and empty`() {
        assertEquals(listOf(Full, Full, Half, Empty, Empty), dsRatingStars(2.5f, 5))
        assertEquals(listOf(Empty, Empty, Empty, Empty, Empty), dsRatingStars(0f, 5))
        assertEquals(listOf(Full, Full, Full, Full, Full), dsRatingStars(5f, 5))
        assertEquals(listOf(Full, Full, Full, Half), dsRatingStars(3.3f, 4))
    }

    @Test
    fun `there are always max stars and at least one`() {
        assertEquals(10, dsRatingStars(7.5f, 10).size)
        assertEquals(listOf(Full), dsRatingStars(3f, 1))
        assertEquals(listOf(Full), dsRatingStars(3f, 0))
        assertEquals(listOf(Empty), dsRatingStars(-1f, -3))
    }

    @Test
    fun `a leading tap selects the half only when halves are allowed`() {
        assertEquals(2.5f, dsRatingValueForTap(2, leadingHalf = true, allowHalf = true))
        assertEquals(3f, dsRatingValueForTap(2, leadingHalf = true, allowHalf = false))
        assertEquals(0.5f, dsRatingValueForTap(0, leadingHalf = true, allowHalf = true))
    }

    @Test
    fun `a trailing tap selects the whole star`() {
        assertEquals(3f, dsRatingValueForTap(2, leadingHalf = false, allowHalf = true))
        assertEquals(3f, dsRatingValueForTap(2, leadingHalf = false, allowHalf = false))
    }
}
