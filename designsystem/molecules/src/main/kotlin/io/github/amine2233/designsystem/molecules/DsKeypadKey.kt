package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

enum class DsKeyTone { Default, Secondary, Destructive }

@Composable
fun DsKeypadKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tone: DsKeyTone = DsKeyTone.Default,
) {
    val c = DsTheme.colors
    val (bg, fg, border) =
        when (tone) {
            DsKeyTone.Default -> Triple(c.surface, c.textPrimary, c.border)
            DsKeyTone.Secondary -> Triple(c.surface, c.textSecondary, c.border)
            DsKeyTone.Destructive -> Triple(c.errorContainer, c.error, c.errorBorder)
        }
    Box(
        modifier =
            modifier
                .heightIn(min = DsTheme.spacing.minTouchTarget)
                .clip(DsShapes.key)
                .background(bg)
                .border(1.dp, border, DsShapes.key)
                .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, fontSize = 22.sp, fontWeight = FontWeight.Medium, color = fg)
    }
}

@DsComponentPreview
@Composable
private fun DsKeypadKeyPreview() =
    DsPreview {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DsKeypadKey("7", {}, Modifier.weight(1f).height(48.dp))
            DsKeypadKey(",", {}, Modifier.weight(1f).height(48.dp), tone = DsKeyTone.Secondary)
            DsKeypadKey("⌫", {}, Modifier.weight(1f).height(48.dp), tone = DsKeyTone.Destructive)
        }
    }
