package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

@Composable
fun DsDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
) {
    HorizontalDivider(modifier = modifier, thickness = thickness, color = DsTheme.colors.border)
}

@Composable
fun DsVerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
) {
    VerticalDivider(modifier = modifier, thickness = thickness, color = DsTheme.colors.border)
}

@DsComponentPreview
@Composable
private fun DsDividerPreview() =
    DsPreview {
        DsText("Above")
        DsDivider(Modifier.padding(vertical = 8.dp))
        Row(Modifier.height(24.dp)) {
            DsText("Left")
            DsVerticalDivider(Modifier.padding(horizontal = 8.dp))
            DsText("Right")
        }
    }
