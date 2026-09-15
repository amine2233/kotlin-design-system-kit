package io.github.amine2233.designsystem.core

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

object DsTheme {
    val colors: DsColorScheme
        @Composable @ReadOnlyComposable
        get() = LocalDsColors.current
    val typography: DsTypography
        @Composable @ReadOnlyComposable
        get() = LocalDsTypography.current
    val spacing: DsSpacing get() = DsSpacing
    val shapes: DsShapes get() = DsShapes
    val windowSize: DsWindowSize
        @Composable @ReadOnlyComposable
        get() = DsWindowSize.current()
}

@Composable
fun DsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colors: DsColorScheme = if (darkTheme) DsDarkColors else DsLightColors,
    typography: DsTypography = DsDefaultTypography,
    animationsEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalDsColors provides colors,
        LocalDsTypography provides typography,
        LocalDsAnimationsEnabled provides animationsEnabled,
    ) {
        // Material3 bridge so M3 primitives (sheets, dialogs, ripples) follow the DS palette.
        MaterialTheme(
            colorScheme = colors.toMaterial(darkTheme),
            typography = typography.toMaterial(),
            shapes =
                Shapes(
                    extraSmall = DsShapes.xs,
                    small = DsShapes.sm,
                    medium = DsShapes.md,
                    large = DsShapes.lg,
                    extraLarge = DsShapes.xl,
                ),
            content = content,
        )
    }
}

private fun DsColorScheme.toMaterial(dark: Boolean) =
    (if (dark) darkColorScheme() else lightColorScheme()).copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = primary,
        secondary = primary,
        onSecondary = onPrimary,
        secondaryContainer = primaryContainer,
        onSecondaryContainer = primary,
        background = background,
        onBackground = textPrimary,
        surface = surface,
        onSurface = textPrimary,
        surfaceVariant = surfaceSubtle,
        onSurfaceVariant = textSecondary,
        surfaceContainer = surface,
        surfaceContainerLow = surfaceSubtle,
        surfaceContainerHigh = surfaceMuted,
        outline = borderStrong,
        outlineVariant = border,
        error = error,
        onError = onPrimary,
        errorContainer = errorContainer,
        onErrorContainer = error,
        scrim = scrim,
    )

private fun DsTypography.toMaterial() =
    Typography(
        displayLarge = displayLarge,
        displayMedium = displayMedium,
        headlineMedium = headline,
        titleLarge = titleLarge,
        titleMedium = title,
        bodyLarge = bodyLarge,
        bodyMedium = body,
        labelLarge = labelStrong,
        labelMedium = label,
        labelSmall = caption,
    )
