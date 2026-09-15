package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsCircularProgress
import io.github.amine2233.designsystem.core.DsTheme

/** Terminal / NFC waiting state: icon inside a progress ring, headline + hint ("Présentez la carte"). */
@Composable
fun DsProcessingState(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    progress: Float? = null,
    amount: String? = null,
    amountLabel: String? = null,
) {
    val c = DsTheme.colors
    Column(modifier.fillMaxWidth().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (amountLabel != null) Text(amountLabel, style = DsTheme.typography.overline, color = c.textTertiary)
        if (amount != null) {
            Spacer(Modifier.height(4.dp))
            Text(amount, style = DsTheme.typography.displayLarge, color = c.textPrimary)
        }
        Spacer(Modifier.height(24.dp))
        Box(contentAlignment = Alignment.Center) {
            DsCircularProgress(progress, size = 132.dp, strokeWidth = 4.dp)
            Icon(icon, contentDescription = null, tint = c.primary, modifier = Modifier.size(52.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(title, style = DsTheme.typography.titleLarge, color = c.textPrimary, textAlign = TextAlign.Center)
        if (description != null) {
            Spacer(Modifier.height(6.dp))
            Text(description, style = DsTheme.typography.body, color = c.textSecondary, textAlign = TextAlign.Center)
        }
    }
}
