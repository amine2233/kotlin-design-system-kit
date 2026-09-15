package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DsBottomSheet(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    content: @Composable ColumnScope.() -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
        sheetState = sheetState,
        shape = DsShapes.sheet,
        containerColor = DsTheme.colors.surface,
        scrimColor = DsTheme.colors.scrim,
        dragHandle = { DsSheetHandle() },
        content = content,
    )
}

@Composable
fun DsSheetHandle(modifier: Modifier = Modifier) {
    Box(
        modifier
            .padding(top = 10.dp, bottom = 6.dp)
            .width(36.dp)
            .height(4.dp)
            .background(DsTheme.colors.border, DsShapes.pill),
    )
}

@DsComponentPreview
@Composable
private fun DsSheetHandlePreview() = DsPreview {
    DsSheetHandle()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(name = "sheet", device = "spec:width=375dp,height=780dp,dpi=420")
@Composable
private fun DsBottomSheetPreview() = DsTheme {
    DsBottomSheet(onDismiss = {}, sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)) {
        Text("Contenu du feuillet", style = DsTheme.typography.title, modifier = Modifier.padding(16.dp))
    }
}
