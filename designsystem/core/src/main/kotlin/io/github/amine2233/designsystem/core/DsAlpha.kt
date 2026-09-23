package io.github.amine2233.designsystem.core

/**
 * Opacity steps for tinted surfaces.
 *
 * A tone drawn at [tint] behind content and at [border] as a hairline keeps one hue doing both jobs,
 * and — being translucent — the result sits correctly on any surface, light or dark, instead of
 * needing a second opaque color per theme.
 */
object DsAlpha {
    const val TINT = 0.12f
    const val BORDER = 0.35f
}
