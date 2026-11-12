package io.github.amine2233.designsystem.atoms

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class DsSpinnerTest {
    @Test
    fun `sizes grow from 14 to 30 dp in four dp steps`() {
        assertEquals(
            listOf(14.dp, 18.dp, 22.dp, 26.dp, 30.dp),
            DsSpinnerSize.entries.map { it.diameter },
        )
    }

    @Test
    fun `stroke is an eighth of the diameter`() {
        DsSpinnerSize.entries.forEach { assertEquals(it.diameter / 8, it.strokeWidth) }
    }
}
