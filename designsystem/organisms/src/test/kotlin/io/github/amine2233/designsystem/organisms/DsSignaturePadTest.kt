package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class DsSignaturePadTest {
    private val a = DsSignaturePoint(0.1f, 0.2f)
    private val b = DsSignaturePoint(0.3f, 0.4f)
    private val c = DsSignaturePoint(0.5f, 0.6f)

    @Test
    fun `startStroke on an empty list adds a one-point stroke`() {
        assertEquals(listOf(DsSignatureStroke(listOf(a))), emptyList<DsSignatureStroke>().startStroke(a))
    }

    @Test
    fun `startStroke on a non-empty list appends a new stroke`() {
        val strokes = listOf(DsSignatureStroke(listOf(a, b)))
        assertEquals(listOf(DsSignatureStroke(listOf(a, b)), DsSignatureStroke(listOf(c))), strokes.startStroke(c))
    }

    @Test
    fun `addPoint appends to the last stroke only`() {
        val strokes = listOf(DsSignatureStroke(listOf(a)), DsSignatureStroke(listOf(b)))
        assertEquals(listOf(DsSignatureStroke(listOf(a)), DsSignatureStroke(listOf(b, c))), strokes.addPoint(c))
    }

    @Test
    fun `addPoint on an empty list starts a stroke`() {
        assertEquals(listOf(DsSignatureStroke(listOf(a))), emptyList<DsSignatureStroke>().addPoint(a))
    }

    @Test
    fun `startStroke and addPoint do not mutate their input`() {
        val strokes = listOf(DsSignatureStroke(listOf(a)))
        val snapshot = listOf(DsSignatureStroke(listOf(a)))
        strokes.startStroke(b)
        strokes.addPoint(c)
        assertEquals(snapshot, strokes)
    }

    @Test
    fun `dsSignaturePoint divides by the pad size inside bounds`() {
        assertEquals(DsSignaturePoint(0.25f, 0.5f), dsSignaturePoint(50f, 50f, 200f, 100f))
    }

    @Test
    fun `dsSignaturePoint clamps negative positions to zero`() {
        assertEquals(DsSignaturePoint(0f, 0.5f), dsSignaturePoint(-10f, 50f, 200f, 100f))
        assertEquals(DsSignaturePoint(0.25f, 0f), dsSignaturePoint(50f, -10f, 200f, 100f))
    }

    @Test
    fun `dsSignaturePoint clamps positions beyond the pad to one`() {
        assertEquals(DsSignaturePoint(1f, 0.5f), dsSignaturePoint(250f, 50f, 200f, 100f))
        assertEquals(DsSignaturePoint(0.25f, 1f), dsSignaturePoint(50f, 150f, 200f, 100f))
    }

    @Test
    fun `dsSignaturePoint rejects a zero width or height`() {
        assertThrows(IllegalArgumentException::class.java) { dsSignaturePoint(0f, 0f, 0f, 100f) }
        assertThrows(IllegalArgumentException::class.java) { dsSignaturePoint(0f, 0f, 200f, 0f) }
    }

    @Test
    fun `an empty list is not signed`() {
        assertFalse(emptyList<DsSignatureStroke>().isSigned)
    }

    @Test
    fun `point-less strokes are not signed`() {
        assertFalse(listOf(DsSignatureStroke(emptyList()), DsSignatureStroke(emptyList())).isSigned)
    }

    @Test
    fun `a single point is signed`() {
        assertTrue(listOf(DsSignatureStroke(emptyList()), DsSignatureStroke(listOf(a))).isSigned)
    }
}
