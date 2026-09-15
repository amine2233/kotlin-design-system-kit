package io.github.amine2233.designsystem.atoms

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsTheme

@Composable
fun DsDivider(modifier: Modifier = Modifier, thickness: Dp = 1.dp) {
    HorizontalDivider(modifier = modifier, thickness = thickness, color = DsTheme.colors.border)
}

@Composable
fun DsVerticalDivider(modifier: Modifier = Modifier, thickness: Dp = 1.dp) {
    VerticalDivider(modifier = modifier, thickness = thickness, color = DsTheme.colors.border)
}
