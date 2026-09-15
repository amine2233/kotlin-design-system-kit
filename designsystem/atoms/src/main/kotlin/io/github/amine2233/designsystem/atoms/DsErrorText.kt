package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Short inline error ("Montant invalide") under a field or a keypad. Keep messages short per the content rules. */
@Composable
fun DsErrorText(
    message: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(DsIcons.Error, contentDescription = null, tint = DsTheme.colors.error, modifier = Modifier.size(14.dp))
        Text(message, style = DsTheme.typography.caption, color = DsTheme.colors.error)
    }
}

@DsComponentPreview
@Composable
private fun DsErrorTextPreview() =
    DsPreview {
        DsErrorText("Montant invalide")
    }
