package io.github.amine2233.designsystem.organisms

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class DsLoadingHudTest {
    @Test
    fun `loading shows the spinner, not a mark`() {
        assertNull(dsLoadingHudOutcome(DsLoadingHudState.Loading))
    }

    @Test
    fun `success and failure map to their result marks`() {
        assertEquals(DsPaymentOutcome.Success, dsLoadingHudOutcome(DsLoadingHudState.Success))
        assertEquals(DsPaymentOutcome.Failure, dsLoadingHudOutcome(DsLoadingHudState.Failure))
    }

    @Test
    fun `message wins over the description`() {
        assertEquals("Paiement en cours", dsLoadingHudLabel(DsLoadingHudState.Loading, "Paiement en cours", "Patientez"))
    }

    @Test
    fun `description is used when the message is null or blank`() {
        assertEquals("Patientez", dsLoadingHudLabel(DsLoadingHudState.Loading, null, "Patientez"))
        assertEquals("Patientez", dsLoadingHudLabel(DsLoadingHudState.Loading, "  ", "Patientez"))
    }

    @Test
    fun `each state falls back to its own default`() {
        assertEquals("Chargement", dsLoadingHudLabel(DsLoadingHudState.Loading, null, null))
        assertEquals("Succès", dsLoadingHudLabel(DsLoadingHudState.Success, null, null))
        assertEquals("Échec", dsLoadingHudLabel(DsLoadingHudState.Failure, "", " "))
    }
}
