package io.github.amine2233.designsystem.molecules

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
import io.github.amine2233.designsystem.atoms.DsCard
import io.github.amine2233.designsystem.core.DsTheme

/** Card row with optional leading/trailing slots; selected = teal border + tint (article picker, payment method). */
@Composable
fun DsListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    selected: Boolean = false,
    onClick: (() -> Unit)? = null,
    leading: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
) {
    val c = DsTheme.colors
    DsCard(modifier = modifier.fillMaxWidth(), onClick = onClick, selected = selected) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            leading?.invoke()
            Column(Modifier.weight(1f)) {
                Text(
                    headline,
                    style = if (selected) DsTheme.typography.bodyStrong else DsTheme.typography.bodyStrong,
                    color = if (selected) c.primary else c.textPrimary,
                    maxLines = 1,
                )
                if (supporting != null) {
                    Text(
                        supporting,
                        style = DsTheme.typography.caption,
                        color = if (selected) c.primary.copy(alpha = 0.8f) else c.textTertiary,
                        maxLines = 1,
                    )
                }
            }
            trailing?.invoke()
        }
    }
}
