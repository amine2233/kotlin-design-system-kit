package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsBadge
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Grid product tile; [inCartCount] > 0 shows teal border + badge. */
@Composable
fun DsProductCard(
    name: String,
    price: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    inCartCount: Int = 0,
    onLongClick: (() -> Unit)? = null,
    image: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    val selected = inCartCount > 0
    DsCard(
        modifier = modifier,
        onClick = onClick,
        selected = selected,
        containerColor = if (selected) c.primaryTint else c.surface,
    ) {
        Box {
            Column {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .background(if (selected) c.primaryContainerBorder else c.surfaceMuted),
                    contentAlignment = Alignment.Center,
                ) { image?.invoke() }
                Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                    Text(name, style = DsTheme.typography.bodyStrong, color = c.textPrimary, maxLines = 1)
                    Text(price, style = DsTheme.typography.labelStrong, color = c.primary, maxLines = 1)
                }
            }
            if (selected) {
                DsBadge(inCartCount.toString(), Modifier.align(Alignment.TopEnd).padding(6.dp))
            }
        }
    }
}

@DsComponentPreview
@Composable
private fun DsProductCardPreview() = DsPreview {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        DsProductCard("Espresso", "150 DA", onClick = {}, modifier = Modifier.weight(1f))
        DsProductCard("Cappuccino", "200 DA", onClick = {}, inCartCount = 2, modifier = Modifier.weight(1f))
    }
}
