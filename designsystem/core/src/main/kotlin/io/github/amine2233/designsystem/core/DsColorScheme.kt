package io.github.amine2233.designsystem.core

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class DsColorScheme(
    val primary: Color,
    val primaryPressed: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val primaryContainerBorder: Color,
    val primaryTint: Color,

    val background: Color,
    val surface: Color,
    val surfaceSubtle: Color,
    val surfaceMuted: Color,
    val scrim: Color,

    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,

    val border: Color,
    val borderStrong: Color,

    val error: Color,
    val errorContainer: Color,
    val errorBorder: Color,
    val warning: Color,
    val warningContainer: Color,
    val warningBorder: Color,
    val success: Color,
    val successContainer: Color,
    val info: Color,
    val infoContainer: Color,
)

val DsLightColors = DsColorScheme(
    primary = DsPalette.Teal700,
    primaryPressed = DsPalette.Teal800,
    onPrimary = DsPalette.Grey0,
    primaryContainer = DsPalette.Teal50,
    primaryContainerBorder = DsPalette.Teal100,
    primaryTint = DsPalette.Teal25,
    background = DsPalette.Grey50,
    surface = DsPalette.Grey0,
    surfaceSubtle = DsPalette.Grey50,
    surfaceMuted = DsPalette.Grey100,
    scrim = Color(0x5C000000),
    textPrimary = DsPalette.Grey1000,
    textSecondary = DsPalette.Grey500,
    textTertiary = DsPalette.Grey400,
    textDisabled = DsPalette.Grey300,
    border = DsPalette.Grey200,
    borderStrong = DsPalette.Grey300,
    error = DsPalette.Red500,
    errorContainer = DsPalette.Red50,
    errorBorder = DsPalette.Red100,
    warning = DsPalette.Orange700,
    warningContainer = DsPalette.Orange50,
    warningBorder = DsPalette.Orange100,
    success = DsPalette.Green700,
    successContainer = DsPalette.Green50,
    info = DsPalette.Blue500,
    infoContainer = DsPalette.Blue50,
)

val DsDarkColors = DsLightColors.copy(
    primary = DsPalette.Teal400,
    primaryPressed = DsPalette.Teal200,
    onPrimary = DsPalette.Grey1000,
    primaryContainer = DsPalette.Teal900,
    primaryContainerBorder = DsPalette.Teal800,
    primaryTint = Color(0xFF12302B),
    background = DsPalette.Grey1000,
    surface = DsPalette.Grey900,
    surfaceSubtle = DsPalette.Grey800,
    surfaceMuted = DsPalette.Grey700,
    scrim = Color(0x99000000),
    textPrimary = DsPalette.Grey0,
    textSecondary = DsPalette.Grey400,
    textTertiary = DsPalette.Grey500,
    textDisabled = DsPalette.Grey700,
    border = DsPalette.Grey800,
    borderStrong = DsPalette.Grey700,
    errorContainer = Color(0xFF3A1210),
    errorBorder = Color(0xFF5C1E1A),
    warningContainer = Color(0xFF3A2410),
    warningBorder = Color(0xFF5C3A1A),
    successContainer = Color(0xFF10301A),
    infoContainer = Color(0xFF10283A),
)

val LocalDsColors = staticCompositionLocalOf { DsLightColors }
