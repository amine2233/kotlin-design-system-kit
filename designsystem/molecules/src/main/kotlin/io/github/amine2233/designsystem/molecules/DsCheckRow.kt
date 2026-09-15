package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsCheckBullet
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** Value-prop bullet row from onboarding. */
@Composable
fun DsCheckRow(
    text: String,
    modifier: Modifier = Modifier,
    filled: Boolean = true,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        DsCheckBullet(filled = filled)
        Text(text, style = DsTheme.typography.body, color = DsTheme.colors.textPrimary)
    }
}

@DsComponentPreview
@Composable
private fun DsCheckRowPreview() =
    DsPreview {
        DsCheckRow("Encaissement rapide et fiable")
        DsCheckRow("TVA multi-taux, remises et pourboires", filled = false)
    }
