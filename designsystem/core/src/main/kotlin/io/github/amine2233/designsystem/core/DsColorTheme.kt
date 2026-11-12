package io.github.amine2233.designsystem.core

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Selectable brand palettes. Alternatives only swap the primary family; every other slot is the default's. */
enum class DsColorTheme {
    Teal,
    Blue,
    Purple,
    ;

    fun colors(dark: Boolean): DsColorScheme =
        when (this) {
            Teal -> if (dark) DsDarkColors else DsLightColors
            Blue -> if (dark) DsBlueDarkColors else DsBlueLightColors
            Purple -> if (dark) DsPurpleDarkColors else DsPurpleLightColors
        }
}

private val DsBlueLightColors =
    DsLightColors.copy(
        primary = DsPalette.Cobalt600,
        primaryPressed = DsPalette.Cobalt700,
        onPrimary = DsPalette.Grey0,
        primaryContainer = DsPalette.Cobalt50,
        primaryContainerBorder = DsPalette.Cobalt100,
        primaryTint = DsPalette.Cobalt25,
    )

private val DsBlueDarkColors =
    DsDarkColors.copy(
        primary = DsPalette.Cobalt300,
        primaryPressed = DsPalette.Cobalt200,
        onPrimary = DsPalette.Grey1000,
        primaryContainer = DsPalette.Cobalt900,
        primaryContainerBorder = DsPalette.Cobalt800,
        primaryTint = DsPalette.Cobalt950,
    )

private val DsPurpleLightColors =
    DsLightColors.copy(
        primary = DsPalette.Purple600,
        primaryPressed = DsPalette.Purple700,
        onPrimary = DsPalette.Grey0,
        primaryContainer = DsPalette.Purple50,
        primaryContainerBorder = DsPalette.Purple100,
        primaryTint = DsPalette.Purple25,
    )

private val DsPurpleDarkColors =
    DsDarkColors.copy(
        primary = DsPalette.Purple300,
        primaryPressed = DsPalette.Purple200,
        onPrimary = DsPalette.Grey1000,
        primaryContainer = DsPalette.Purple900,
        primaryContainerBorder = DsPalette.Purple800,
        primaryTint = DsPalette.Purple950,
    )

@DsComponentPreview
@Composable
private fun DsColorThemePreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DsColorTheme.entries.forEach { theme ->
                val scheme = theme.colors(dark = false)
                Text(theme.name, style = DsTheme.typography.labelStrong, color = DsTheme.colors.textPrimary)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(
                        "primary" to scheme.primary,
                        "pressed" to scheme.primaryPressed,
                        "onPrimary" to scheme.onPrimary,
                        "container" to scheme.primaryContainer,
                        "border" to scheme.primaryContainerBorder,
                        "tint" to scheme.primaryTint,
                    ).forEach { (name, color) ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(Modifier.size(40.dp).background(color, DsShapes.sm).border(1.dp, DsTheme.colors.border, DsShapes.sm))
                            Text(name, style = DsTheme.typography.caption, color = DsTheme.colors.textSecondary)
                        }
                    }
                }
            }
        }
    }
