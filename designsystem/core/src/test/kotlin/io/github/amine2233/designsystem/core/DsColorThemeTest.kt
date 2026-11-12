package io.github.amine2233.designsystem.core

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class DsColorThemeTest {
    private val alternatives = DsColorTheme.entries - DsColorTheme.Teal
    private val modes = listOf(false, true)

    @Test
    fun `teal is the default light and dark scheme`() {
        assertSame(DsLightColors, DsColorTheme.Teal.colors(dark = false))
        assertSame(DsDarkColors, DsColorTheme.Teal.colors(dark = true))
    }

    @Test
    fun `alternative themes share every slot outside the primary family`() {
        for (theme in alternatives) {
            for (dark in modes) {
                val default = DsColorTheme.Teal.colors(dark)
                val expected =
                    theme.colors(dark).copy(
                        primary = default.primary,
                        primaryPressed = default.primaryPressed,
                        onPrimary = default.onPrimary,
                        primaryContainer = default.primaryContainer,
                        primaryContainerBorder = default.primaryContainerBorder,
                        primaryTint = default.primaryTint,
                    )
                assertEquals("$theme dark=$dark", default, expected)
            }
        }
    }

    @Test
    fun `every theme reaches WCAG AA on the primary pairs`() {
        for (theme in DsColorTheme.entries) {
            for (dark in modes) {
                val scheme = theme.colors(dark)
                val onPrimary = contrast(scheme.onPrimary, scheme.primary)
                val onContainer = contrast(scheme.primary, scheme.primaryContainer)
                assertTrue("$theme dark=$dark onPrimary/primary $onPrimary", onPrimary >= 4.5)
                assertTrue("$theme dark=$dark primary/primaryContainer $onContainer", onContainer >= 4.5)
            }
        }
    }

    @Test
    fun `alternative themes change the primary`() {
        for (theme in alternatives) {
            for (dark in modes) {
                assertNotEquals("$theme dark=$dark", DsColorTheme.Teal.colors(dark).primary, theme.colors(dark).primary)
            }
        }
    }

    private fun contrast(
        a: Color,
        b: Color,
    ): Double {
        val lighter = maxOf(a.luminance(), b.luminance())
        val darker = minOf(a.luminance(), b.luminance())
        return (lighter + 0.05) / (darker + 0.05)
    }
}
