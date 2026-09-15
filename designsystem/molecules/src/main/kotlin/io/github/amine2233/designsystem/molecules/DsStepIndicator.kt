package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** "Panier (1) → Pourboire (2) → Paiement (3)". [currentStep] is 0-based; previous steps render as done. */
@Composable
fun DsStepIndicator(
    steps: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier,
) {
    val c = DsTheme.colors
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        steps.forEachIndexed { index, label ->
            val done = index < currentStep
            val active = index == currentStep
            if (index > 0) {
                Box(
                    Modifier
                        .weight(1f)
                        .height(2.dp)
                        .padding(horizontal = 0.dp)
                        .background(if (done || active) c.primary else c.border),
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(horizontal = 8.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .size(22.dp)
                            .then(if (active) Modifier.border(3.dp, c.primaryContainerBorder, DsShapes.pill) else Modifier)
                            .padding(if (active) 0.dp else 0.dp)
                            .background(if (done || active) c.primary else c.surface, DsShapes.pill)
                            .then(if (!done && !active) Modifier.border(1.5.dp, c.borderStrong, DsShapes.pill) else Modifier),
                    contentAlignment = Alignment.Center,
                ) {
                    if (done) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = c.onPrimary, modifier = Modifier.size(12.dp))
                    } else {
                        Text(
                            (index + 1).toString(),
                            style = DsTheme.typography.overline.copy(fontWeight = if (active) FontWeight.Bold else FontWeight.Normal),
                            color = if (active) c.onPrimary else c.textDisabled,
                        )
                    }
                }
                Text(
                    label,
                    style = DsTheme.typography.caption.copy(fontWeight = if (active) FontWeight.Bold else FontWeight.Medium),
                    color = if (done || active) c.primary else c.textDisabled,
                    maxLines = 1,
                )
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsStepIndicatorPreview() =
    DsPreview {
        DsStepIndicator(listOf("Panier", "Pourboire", "Paiement"), currentStep = 0)
        DsStepIndicator(listOf("Panier", "Pourboire", "Paiement"), currentStep = 1)
        DsStepIndicator(listOf("Panier", "Pourboire", "Paiement"), currentStep = 2)
    }
