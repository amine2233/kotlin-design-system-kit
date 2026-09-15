package io.github.amine2233.designsystem.core

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp

/** Phone portrait = Compact, tablet / landscape ≥ 600dp = Expanded. */
enum class DsWindowSize {
    Compact,
    Expanded;

    val isExpanded: Boolean get() = this == Expanded

    companion object {
        val expandedBreakpoint = 600.dp

        @Composable
        @ReadOnlyComposable
        fun current(): DsWindowSize {
            val widthPx = LocalWindowInfo.current.containerSize.width
            val widthDp = with(LocalDensity.current) { widthPx.toDp() }
            return if (widthDp >= expandedBreakpoint) Expanded else Compact
        }
    }
}
