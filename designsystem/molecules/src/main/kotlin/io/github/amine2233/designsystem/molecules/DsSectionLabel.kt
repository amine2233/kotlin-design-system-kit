package io.github.amine2233.designsystem.molecules

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.amine2233.designsystem.core.DsTheme

/** Uppercase overline ("APPLIQUER À", "MONTANT RAPIDE"). */
@Composable
fun DsSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(text.uppercase(), style = DsTheme.typography.overline, color = DsTheme.colors.textTertiary, modifier = modifier)
}
