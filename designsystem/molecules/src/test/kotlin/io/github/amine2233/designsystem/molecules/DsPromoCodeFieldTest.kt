package io.github.amine2233.designsystem.molecules

import org.junit.Assert.assertEquals
import org.junit.Test

class DsPromoCodeFieldTest {
    @Test
    fun `the applied description reads the code and what it gives`() {
        assertEquals("Code PROMO10 appliqué, −10 %", dsPromoCodeAppliedDescription("PROMO10", "−10 %"))
    }

    @Test
    fun `the remove description names the code`() {
        assertEquals("Retirer le code PROMO10", dsPromoCodeRemoveDescription("PROMO10"))
    }
}
