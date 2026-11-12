package io.github.amine2233.designsystem.atoms

import io.github.amine2233.designsystem.core.DsDarkColors
import io.github.amine2233.designsystem.core.DsLightColors
import org.junit.Assert.assertEquals
import org.junit.Test

class DsStatusDotTest {
    @Test
    fun `light presence colours come from the scheme`() {
        assertEquals(DsLightColors.success, dsPresenceColor(DsPresence.Online, DsLightColors))
        assertEquals(DsLightColors.warning, dsPresenceColor(DsPresence.Away, DsLightColors))
        assertEquals(DsLightColors.error, dsPresenceColor(DsPresence.Busy, DsLightColors))
        assertEquals(DsLightColors.textTertiary, dsPresenceColor(DsPresence.Offline, DsLightColors))
    }

    @Test
    fun `dark presence colours come from the scheme`() {
        assertEquals(DsDarkColors.success, dsPresenceColor(DsPresence.Online, DsDarkColors))
        assertEquals(DsDarkColors.warning, dsPresenceColor(DsPresence.Away, DsDarkColors))
        assertEquals(DsDarkColors.error, dsPresenceColor(DsPresence.Busy, DsDarkColors))
        assertEquals(DsDarkColors.textTertiary, dsPresenceColor(DsPresence.Offline, DsDarkColors))
    }
}
