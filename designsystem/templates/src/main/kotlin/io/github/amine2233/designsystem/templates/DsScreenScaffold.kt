package io.github.amine2233.designsystem.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsDivider
import io.github.amine2233.designsystem.core.DsScreenPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.organisms.DsTopBar

/** Top bar · scrolling body · pinned bottom panel (totals + CTA) on white with a hairline. */
@Composable
fun DsScreenScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomPanel: (@Composable ColumnScope.() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val c = DsTheme.colors
    Scaffold(
        modifier = modifier,
        containerColor = c.background,
        topBar = topBar,
        bottomBar = {
            if (bottomPanel != null) {
                Column(Modifier.fillMaxWidth().background(c.surface)) {
                    DsDivider(thickness = 1.5.dp)
                    Column(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), content = bottomPanel)
                }
            }
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding), content = content)
    }
}

@DsScreenPreview
@Composable
private fun DsScreenScaffoldPreview() =
    DsTheme {
        DsScreenScaffold(
            topBar = { DsTopBar("Écran", onBack = {}) },
            bottomPanel = { DsButton("Continuer", onClick = {}, modifier = Modifier.fillMaxWidth()) },
        ) { Text("Contenu", style = DsTheme.typography.body, modifier = Modifier.padding(16.dp)) }
    }
