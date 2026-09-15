package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.atoms.DsChip
import io.github.amine2233.designsystem.atoms.DsChipTone
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsElevation
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsQuantityStepper

/** Cart row: stepper + name + price + remove, then TVA chip · unit price · discount chip. */
@Composable
fun DsCartLineItem(
    name: String,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    lineTotal: String,
    unitPrice: String,
    tvaLabel: String,
    modifier: Modifier = Modifier,
    note: String? = null,
    originalTotal: String? = null,
    discountLabel: String? = null,
    onTvaClick: () -> Unit = {},
    onDiscountClick: () -> Unit = {},
    onRemove: (() -> Unit)? = null,
) {
    val c = DsTheme.colors
    val discounted = originalTotal != null
    DsCard(
        modifier = modifier.fillMaxWidth(),
        borderColor = if (discounted) c.warningBorder else c.border,
        borderWidth = if (discounted) 1.5.dp else 1.dp,
        elevation = DsElevation.card,
    ) {
        Column(Modifier.padding(horizontal = 10.dp, vertical = 9.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                DsQuantityStepper(quantity, onQuantityChange)
                Column(Modifier.weight(1f)) {
                    Text(name, style = DsTheme.typography.bodyStrong, color = c.textPrimary, maxLines = 1)
                    if (note != null) Text("Note : $note", style = DsTheme.typography.caption, color = c.textSecondary, maxLines = 1)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(lineTotal, style = DsTheme.typography.title, color = c.textPrimary)
                    if (originalTotal != null) {
                        Text(originalTotal, style = DsTheme.typography.caption.copy(textDecoration = TextDecoration.LineThrough), color = c.warning)
                    }
                }
                if (onRemove != null) {
                    DsIconButton(Icons.Default.Close, contentDescription = "Supprimer", onClick = onRemove, tint = c.textDisabled, modifier = Modifier.size(28.dp))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                DsChip("$tvaLabel ▾", tone = DsChipTone.Primary, onClick = onTvaClick, contentPadding = PaddingValues(horizontal = 10.dp, vertical = 3.dp))
                Text("$unitPrice / u.", style = DsTheme.typography.caption, color = c.textTertiary)
                Spacer(Modifier.weight(1f))
                DsChip(discountLabel ?: "% Remise →", tone = DsChipTone.Warning, onClick = onDiscountClick, contentPadding = PaddingValues(horizontal = 10.dp, vertical = 3.dp))
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsCartLineItemPreview() = DsPreview {
    DsCartLineItem("Cappuccino", 2, {}, lineTotal = "400 DA", unitPrice = "200 DA", tvaLabel = "TVA 10%", onRemove = {})
    Spacer(Modifier.height(6.dp))
    DsCartLineItem("Formule Déjeuner", 1, {}, lineTotal = "441 DA", unitPrice = "490 DA", tvaLabel = "TVA 10%", note = "Sans sucre", originalTotal = "490 DA", discountLabel = "−10% ✓", onRemove = {})
}
