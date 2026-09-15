package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Highlighted teal box showing a big amount + unit ("1 500,00 DA", "10,00 %"). */
@Composable
fun DsAmountDisplay(
    amount: String,
    unit: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    prefix: String? = null,
) {
    val c = DsTheme.colors
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(c.primaryContainer, DsShapes.lg)
            .border(2.dp, c.primary, DsShapes.lg)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = if (label != null) Arrangement.SpaceBetween else Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (label != null) Text(label, style = DsTheme.typography.body, color = c.textSecondary)
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (prefix != null) Text(prefix, style = DsTheme.typography.bodyLarge, color = c.primary)
            Text(amount, style = DsTheme.typography.displayMedium, color = c.primary, maxLines = 1)
            Text(unit, style = DsTheme.typography.title, color = c.primary, modifier = Modifier.padding(bottom = 4.dp))
        }
    }
}

@DsComponentPreview
@Composable
private fun DsAmountDisplayPreview() = DsPreview {
    DsAmountDisplay("1 500,00", "DA")
    Spacer(Modifier.height(8.dp))
    DsAmountDisplay("10,00", "%", label = "Valeur saisie")
    Spacer(Modifier.height(8.dp))
    DsAmountDisplay("108,00", "DA", label = "10% de 1 080 DA", prefix = "+")
}
