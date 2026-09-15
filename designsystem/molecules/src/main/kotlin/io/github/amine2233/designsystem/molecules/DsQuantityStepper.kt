package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsVerticalDivider
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** "− 2 +" compact stepper from the cart rows. */
@Composable
fun DsQuantityStepper(
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    min: Int = 0,
    max: Int = Int.MAX_VALUE,
) {
    val c = DsTheme.colors
    Row(
        modifier =
            modifier
                .height(32.dp)
                .clip(DsShapes.sm)
                .background(c.surfaceSubtle)
                .border(1.dp, c.border, DsShapes.sm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        StepKey("−", enabled = quantity > min, color = c.textSecondary) { onQuantityChange(quantity - 1) }
        DsVerticalDivider()
        Box(Modifier.width(28.dp).height(32.dp).background(c.surface), contentAlignment = Alignment.Center) {
            Text(quantity.toString(), style = DsTheme.typography.bodyStrong.copy(fontWeight = FontWeight.SemiBold), color = c.textPrimary)
        }
        DsVerticalDivider()
        StepKey("+", enabled = quantity < max, color = c.primary) { onQuantityChange(quantity + 1) }
    }
}

@Composable
private fun StepKey(
    symbol: String,
    enabled: Boolean,
    color: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Box(
        modifier =
            Modifier
                .width(32.dp)
                .height(32.dp)
                .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            symbol,
            style = DsTheme.typography.titleLarge,
            color = if (enabled) color else DsTheme.colors.textDisabled,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsQuantityStepperPreview() =
    DsPreview {
        DsQuantityStepper(2, {})
        Spacer(Modifier.height(8.dp))
        DsQuantityStepper(0, {}, min = 0)
    }
