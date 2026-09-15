package io.github.amine2233.designsystem.core

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing

/** Material3 default-ish motion; no decorative animations per the design guidelines. */
object DsMotion {
    const val FAST = 150
    const val NORMAL = 250
    const val SLOW = 400
    val standardEasing: Easing = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    val emphasizedEasing: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1f)
}
