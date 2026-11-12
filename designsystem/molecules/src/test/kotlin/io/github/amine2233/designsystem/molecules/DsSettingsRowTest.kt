package io.github.amine2233.designsystem.molecules

import io.github.amine2233.designsystem.core.DsLightColors
import org.junit.Assert.assertEquals
import org.junit.Test

class DsSettingsRowTest {
    private val c = DsLightColors

    @Test
    fun `neutral sits on the subtle surface with a secondary glyph`() {
        assertEquals(c.surfaceSubtle to c.textSecondary, dsSettingsTileColors(DsSettingsTone.Neutral, c))
    }

    @Test
    fun `each tone pairs its container with its own colour`() {
        assertEquals(c.primaryContainer to c.primary, dsSettingsTileColors(DsSettingsTone.Primary, c))
        assertEquals(c.successContainer to c.success, dsSettingsTileColors(DsSettingsTone.Success, c))
        assertEquals(c.warningContainer to c.warning, dsSettingsTileColors(DsSettingsTone.Warning, c))
        assertEquals(c.infoContainer to c.info, dsSettingsTileColors(DsSettingsTone.Info, c))
        assertEquals(c.errorContainer to c.error, dsSettingsTileColors(DsSettingsTone.Error, c))
    }
}
