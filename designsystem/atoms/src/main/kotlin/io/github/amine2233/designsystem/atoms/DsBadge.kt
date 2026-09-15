package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Small pill counter (cart count, step number). */
@Composable
fun DsBadge(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = DsTheme.colors.primary,
    contentColor: Color = DsTheme.colors.onPrimary,
) {
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 20.dp, minHeight = 20.dp)
            .background(containerColor, DsShapes.pill)
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = DsTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
            color = contentColor,
            maxLines = 1,
        )
    }
}

@DsComponentPreview
@Composable
private fun DsBadgePreview() = DsPreview {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { DsBadge("3"); DsBadge("12", containerColor = DsTheme.colors.error); DsBadge("NEW") }
}
