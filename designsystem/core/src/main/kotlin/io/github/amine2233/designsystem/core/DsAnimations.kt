package io.github.amine2233.designsystem.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalInspectionMode

/** Set to false (via `DsTheme(animationsEnabled = false)`) in screenshot tests. */
val LocalDsAnimationsEnabled = staticCompositionLocalOf { true }

/**
 * False in Android Studio previews (`LocalInspectionMode`) and whenever the theme disables animations
 * (screenshot tests). Every DS component with an infinite or one-shot animation uses this as the default
 * for its `animate` / `progress` parameter, so snapshots are deterministic without per-test flags.
 */
val DsTheme.animationsEnabled: Boolean
    @Composable @ReadOnlyComposable
    get() = LocalDsAnimationsEnabled.current && !LocalInspectionMode.current
