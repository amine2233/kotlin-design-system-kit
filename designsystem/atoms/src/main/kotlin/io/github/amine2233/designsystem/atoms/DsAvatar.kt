package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

@Composable
fun DsAvatar(
    initials: String,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(DsTheme.colors.primary, DsShapes.pill),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials.take(2).uppercase(),
            style = DsTheme.typography.label.copy(fontWeight = FontWeight.SemiBold),
            color = DsTheme.colors.onPrimary,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsAvatarPreview() = DsPreview {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { DsAvatar("AK"); DsAvatar("ab", size = 48.dp) }
}
