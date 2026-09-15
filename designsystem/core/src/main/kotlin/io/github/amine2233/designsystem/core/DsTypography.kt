package io.github.amine2233.designsystem.core

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ponytail: design uses DM Sans; system sans-serif until the font is bundled in res/font.
val DsFontFamily: FontFamily = FontFamily.SansSerif

@Immutable
data class DsTypography(
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val headline: TextStyle,
    val titleLarge: TextStyle,
    val title: TextStyle,
    val bodyLarge: TextStyle,
    val body: TextStyle,
    val bodyStrong: TextStyle,
    val label: TextStyle,
    val labelStrong: TextStyle,
    val caption: TextStyle,
    val overline: TextStyle,
)

private fun style(
    size: Int,
    weight: FontWeight,
    lineHeight: Int = size + 8,
    letterSpacing: Float = 0f,
) = TextStyle(
    fontFamily = DsFontFamily,
    fontSize = size.sp,
    fontWeight = weight,
    lineHeight = lineHeight.sp,
    letterSpacing = letterSpacing.sp,
)

val DsDefaultTypography =
    DsTypography(
        displayLarge = style(40, FontWeight.Bold, 44, -0.4f),
        displayMedium = style(30, FontWeight.Bold, 36, -0.3f),
        headline = style(24, FontWeight.Bold, 30, -0.2f),
        titleLarge = style(18, FontWeight.SemiBold, 24),
        title = style(16, FontWeight.SemiBold, 22),
        bodyLarge = style(15, FontWeight.Normal, 22),
        body = style(13, FontWeight.Normal, 18),
        bodyStrong = style(13, FontWeight.Medium, 18),
        label = style(12, FontWeight.Medium, 16),
        labelStrong = style(12, FontWeight.SemiBold, 16),
        caption = style(11, FontWeight.Normal, 14),
        overline = style(10, FontWeight.Medium, 12, 0.6f),
    )

val LocalDsTypography = staticCompositionLocalOf { DsDefaultTypography }
