package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/**
 * Continuous or stepped value selection — a tip percentage, a brightness, a print darkness.
 *
 * [valueText] is the caller's formatted value (currency, percent); the slider never formats
 * numbers itself because the format belongs to the locale, not to the design system.
 */
@Composable
fun DsSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    label: String? = null,
    valueText: String? = null,
    supportingText: String? = null,
    enabled: Boolean = true,
    onValueChangeFinished: (() -> Unit)? = null,
) {
    val c = DsTheme.colors
    DsFormField(
        modifier = modifier,
        label = label,
        supportingText = supportingText,
        trailingSupportingText = valueText,
        enabled = enabled,
    ) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            valueRange = valueRange,
            steps = steps,
            onValueChangeFinished = onValueChangeFinished,
            colors =
                SliderDefaults.colors(
                    thumbColor = c.primary,
                    activeTrackColor = c.primary,
                    activeTickColor = c.onPrimary,
                    inactiveTrackColor = c.border,
                    inactiveTickColor = c.borderStrong,
                    disabledThumbColor = c.textDisabled,
                    disabledActiveTrackColor = c.border,
                    disabledInactiveTrackColor = c.border,
                ),
        )
    }
}

@DsComponentPreview
@Composable
private fun DsSliderPreview() =
    DsPreview {
        DsSlider(0.15f, {}, label = "Pourboire", valueText = "15 %")
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsSlider(3f, {}, valueRange = 1f..5f, steps = 3, label = "Intensité d'impression", valueText = "3 / 5")
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsSlider(0.4f, {}, label = "Désactivé", enabled = false)
    }
