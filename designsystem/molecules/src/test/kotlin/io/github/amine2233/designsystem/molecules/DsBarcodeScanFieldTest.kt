package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DsBarcodeScanFieldTest {
    private fun status(state: DsBarcodeScanState) = dsBarcodeScanStatusText(state, "Article trouvé", "Article introuvable")

    @Test
    fun `idle and scanning have no status`() {
        assertNull(status(DsBarcodeScanState.Idle))
        assertNull(status(DsBarcodeScanState.Scanning))
    }

    @Test
    fun `found reads its message`() {
        assertEquals("Café crème 250 g", status(DsBarcodeScanState.Found("Café crème 250 g")))
    }

    @Test
    fun `found without a message reads the found text`() {
        assertEquals("Article trouvé", status(DsBarcodeScanState.Found()))
    }

    @Test
    fun `not found reads its message`() {
        assertEquals("Code inconnu", status(DsBarcodeScanState.NotFound("Code inconnu")))
    }

    @Test
    fun `not found without a message reads the not found text`() {
        assertEquals("Article introuvable", status(DsBarcodeScanState.NotFound()))
    }
}
