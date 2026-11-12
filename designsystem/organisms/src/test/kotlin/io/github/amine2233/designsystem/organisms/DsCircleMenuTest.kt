package io.github.amine2233.designsystem.organisms

import androidx.compose.ui.geometry.Offset
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

private const val TOLERANCE = 0.001f
private const val R = 100f

class DsCircleMenuTest {
    private fun assertOffset(
        expected: Offset,
        actual: Offset,
    ) {
        assertEquals(expected.x, actual.x, TOLERANCE)
        assertEquals(expected.y, actual.y, TOLERANCE)
    }

    private fun polar(degrees: Double) = Offset((R * cos(Math.toRadians(degrees))).toFloat(), (-R * sin(Math.toRadians(degrees))).toFloat())

    @Test
    fun `first and last slots of a quarter arc land on the start and end angles`() {
        assertOffset(polar(90.0), dsCircleMenuSlot(0, 2, 90f, 90f, R, rtl = false))
        assertOffset(polar(180.0), dsCircleMenuSlot(1, 2, 90f, 90f, R, rtl = false))
    }

    @Test
    fun `full circle spaces four slots 90 degrees apart without overlap`() {
        val slots = List(4) { dsCircleMenuSlot(it, 4, 90f, 360f, R, rtl = false) }
        slots.forEachIndexed { i, slot -> assertOffset(polar(90.0 + 90.0 * i), slot) }
        slots.zipWithNext().forEach { (a, b) ->
            val delta = Math.toDegrees(atan2(-b.y, b.x).toDouble() - atan2(-a.y, a.x).toDouble())
            assertEquals(90.0, (delta + 360.0) % 360.0, TOLERANCE.toDouble())
        }
        for (i in slots.indices) {
            for (j in i + 1 until slots.size) {
                assertTrue((slots[i] - slots[j]).getDistance() > R)
            }
        }
    }

    @Test
    fun `rtl negates x and keeps y`() {
        repeat(3) {
            val ltr = dsCircleMenuSlot(it, 3, 30f, 120f, R, rtl = false)
            val rtl = dsCircleMenuSlot(it, 3, 30f, 120f, R, rtl = true)
            assertEquals(-ltr.x, rtl.x, TOLERANCE)
            assertEquals(ltr.y, rtl.y, TOLERANCE)
        }
    }

    @Test
    fun `zero degrees points to the end edge and ninety degrees points up`() {
        assertOffset(Offset(R, 0f), dsCircleMenuSlot(0, 2, 0f, 90f, R, rtl = false))
        assertOffset(Offset(0f, -R), dsCircleMenuSlot(1, 2, 0f, 90f, R, rtl = false))
    }
}
