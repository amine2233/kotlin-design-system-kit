package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

enum class DsTotalsEmphasis { Muted, Normal, Discount, Positive, Total }

@Composable
fun DsTotalsRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    emphasis: DsTotalsEmphasis = DsTotalsEmphasis.Normal,
    valueColor: Color? = null,
) {
    val c = DsTheme.colors
    val t = DsTheme.typography
    val (labelStyle, valueStyle, color) =
        when (emphasis) {
            DsTotalsEmphasis.Muted -> Triple(t.caption, t.caption, c.textTertiary)
            DsTotalsEmphasis.Normal -> Triple(t.label, t.labelStrong, c.textSecondary)
            DsTotalsEmphasis.Discount -> Triple(t.label, t.labelStrong, c.warning)
            DsTotalsEmphasis.Positive -> Triple(t.labelStrong, t.labelStrong, c.primary)
            DsTotalsEmphasis.Total -> Triple(t.title, t.headline, c.textPrimary)
        }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = if (emphasis == DsTotalsEmphasis.Total) Alignment.Bottom else Alignment.CenterVertically,
    ) {
        Text(label, style = labelStyle, color = color)
        Text(value, style = valueStyle, color = valueColor ?: color)
    }
}

@DsComponentPreview
@Composable
private fun DsTotalsRowPreview() =
    DsPreview {
        DsTotalsRow("Sous-total HT", "1 025 DA")
        DsTotalsRow("TVA 5,5% recalculée", "5 DA", emphasis = DsTotalsEmphasis.Muted)
        DsTotalsRow("Remise totale", "−49 DA", emphasis = DsTotalsEmphasis.Discount)
        DsTotalsRow("Pourboire (10%)", "+ 108 DA", emphasis = DsTotalsEmphasis.Positive)
        DsTotalsRow("Total TTC", "1 080 DA", emphasis = DsTotalsEmphasis.Total)
    }
