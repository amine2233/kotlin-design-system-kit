package io.github.amine2233.designsystem.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsVerticalDivider
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.core.DsWindowSize

/**
 * Tablet: [primary] fills, [secondary] is a fixed-width right pane (cart / form).
 * Phone: only [primary]; caller shows [secondary] via [compactSecondary] (e.g. bottom bar / sheet).
 */
@Composable
fun DsTwoPaneLayout(
    modifier: Modifier = Modifier,
    windowSize: DsWindowSize = DsTheme.windowSize,
    secondaryWidth: Dp = 320.dp,
    primary: @Composable () -> Unit,
    secondary: @Composable () -> Unit,
    compactSecondary: @Composable () -> Unit = {},
) {
    if (windowSize.isExpanded) {
        Row(modifier.fillMaxSize()) {
            Box(Modifier.weight(1f).fillMaxHeight()) { primary() }
            DsVerticalDivider(thickness = 1.5.dp)
            Box(Modifier.width(secondaryWidth).fillMaxHeight().background(DsTheme.colors.surfaceSubtle)) { secondary() }
        }
    } else {
        Column(modifier.fillMaxSize()) {
            Box(Modifier.weight(1f)) { primary() }
            compactSecondary()
        }
    }
}

@DsScreenPreview
@Composable
private fun DsTwoPaneLayoutPreview() =
    DsTheme {
        DsTwoPaneLayout(
            primary = { Text("Primary", style = DsTheme.typography.title, modifier = Modifier.padding(16.dp)) },
            secondary = { Text("Secondary pane", style = DsTheme.typography.title, modifier = Modifier.padding(16.dp)) },
            compactSecondary = { DsButton("Bottom slot", onClick = {}, modifier = Modifier.fillMaxWidth().padding(16.dp)) },
        )
    }
