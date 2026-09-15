package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsBadge
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsButtonSize
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Collapsed phone cart bar: count badge · "Panier ▲ / n articles" · Payer CTA. */
@Composable
fun DsBottomCartBar(
    itemCount: Int,
    total: String,
    onExpand: () -> Unit,
    onPay: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val c = DsTheme.colors
    Column(modifier.fillMaxWidth().background(c.surface)) {
        DsDivider(thickness = 1.5.dp)
        Row(
            Modifier.fillMaxWidth().clickable(onClick = onExpand).padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            DsBadge(itemCount.toString())
            Column(Modifier.weight(1f)) {
                Text("Panier ▲", style = DsTheme.typography.bodyStrong, color = c.textPrimary)
                Text("$itemCount articles", style = DsTheme.typography.caption, color = c.textSecondary)
            }
            DsButton(text = "Payer $total", onClick = onPay, size = DsButtonSize.Medium)
        }
    }
}

@DsComponentPreview
@Composable
private fun DsBottomCartBarPreview() =
    DsPreview {
        DsBottomCartBar(itemCount = 3, total = "737 DA", onExpand = {}, onPay = {})
    }
