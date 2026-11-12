package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.atoms.DsSpinner
import io.github.amine2233.designsystem.atoms.DsSpinnerSize
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** What the caller decided about the code in a [DsBarcodeScanField]; the field never scans nor looks up. */
@Immutable
sealed interface DsBarcodeScanState {
    /** Waiting for a code: the scan button is offered. */
    data object Idle : DsBarcodeScanState

    /** A scan is running: the scan button turns into a spinner, the field stays editable. */
    data object Scanning : DsBarcodeScanState

    /** The code matched an article; [message] (its name) replaces the default found text. */
    data class Found(
        val message: String? = null,
    ) : DsBarcodeScanState

    /** The code matched nothing; [message] replaces the default not-found text. */
    data class NotFound(
        val message: String? = null,
    ) : DsBarcodeScanState
}

/** The status line under the field: the state's message or its default text, none while idle or scanning. */
fun dsBarcodeScanStatusText(
    state: DsBarcodeScanState,
    foundText: String,
    notFoundText: String,
): String? =
    when (state) {
        is DsBarcodeScanState.Found -> state.message ?: foundText
        is DsBarcodeScanState.NotFound -> state.message ?: notFoundText
        DsBarcodeScanState.Idle, DsBarcodeScanState.Scanning -> null
    }

/**
 * A SKU / barcode entry: a text field with a scan button, for a cashier who types or scans a code.
 *
 * The field never drives a scanner itself: [onScan] is the caller's hook to start whatever scanner it has.
 * The keyboard's Search key and a hardware Enter both call [onSubmit], so a keyboard-wedge scanner,
 * which types the code then Enter, submits on its own. The caller sets [state]: the scan button turns
 * into a spinner while [DsBarcodeScanState.Scanning], and found / not found show an icon and a text
 * line, announced politely, so colour is never the only signal.
 */
@Composable
fun DsBarcodeScanField(
    value: String,
    onValueChange: (String) -> Unit,
    state: DsBarcodeScanState,
    onScan: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String? = "Code-barres",
    placeholder: String? = "Scanner ou saisir un code",
    enabled: Boolean = true,
    scanContentDescription: String = "Scanner un code-barres",
    scanningContentDescription: String = "Scan en cours",
    foundText: String = "Article trouvé",
    notFoundText: String = "Article introuvable",
) {
    val c = DsTheme.colors
    val submit = { if (enabled) onSubmit() }
    val found = state is DsBarcodeScanState.Found
    DsFormField(modifier = modifier.fillMaxWidth(), label = label, enabled = enabled) {
        Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs)) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .onPreviewKeyEvent { event ->
                            if (event.key != Key.Enter && event.key != Key.NumPadEnter) return@onPreviewKeyEvent false
                            if (event.type == KeyEventType.KeyDown && event.nativeKeyEvent.repeatCount == 0) submit()
                            true
                        },
                enabled = enabled,
                singleLine = true,
                isError = state is DsBarcodeScanState.NotFound,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { submit() }),
                textStyle = DsTheme.typography.bodyStrong,
                shape = DsShapes.input,
                placeholder = placeholder?.let { { Text(it, style = DsTheme.typography.body, color = c.textTertiary) } },
                trailingIcon = {
                    if (state is DsBarcodeScanState.Scanning) {
                        Box(Modifier.size(DsTheme.spacing.minTouchTarget), contentAlignment = Alignment.Center) {
                            DsSpinner(size = DsSpinnerSize.Small, contentDescription = scanningContentDescription)
                        }
                    } else {
                        DsIconButton(DsIcons.Scan, contentDescription = scanContentDescription, onClick = onScan, enabled = enabled)
                    }
                },
                colors =
                    OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = c.surface,
                        unfocusedContainerColor = c.surfaceSubtle,
                        focusedBorderColor = if (found) c.success else c.primary,
                        unfocusedBorderColor = if (found) c.success else c.border,
                        errorBorderColor = c.error,
                        cursorColor = c.primary,
                        focusedTextColor = c.textPrimary,
                        unfocusedTextColor = c.textPrimary,
                    ),
            )
            val status = dsBarcodeScanStatusText(state, foundText, notFoundText)
            when {
                status == null -> Unit
                state is DsBarcodeScanState.NotFound -> StatusRow(DsIcons.Error, c.error, c.error, status)
                else -> StatusRow(DsIcons.Success, c.success, c.textPrimary, status)
            }
        }
    }
}

@Composable
private fun StatusRow(
    icon: ImageVector,
    iconTint: Color,
    textColor: Color,
    text: String,
) {
    Row(
        modifier =
            Modifier.clearAndSetSemantics {
                contentDescription = text
                liveRegion = LiveRegionMode.Polite
            },
        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(DsTheme.spacing.md))
        Text(text, style = DsTheme.typography.caption, color = textColor)
    }
}

@DsComponentPreview
@Composable
private fun DsBarcodeScanFieldPreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.md)) {
            DsBarcodeScanField("", {}, DsBarcodeScanState.Idle, onScan = {}, onSubmit = {})
            DsBarcodeScanField("3017620422003", {}, DsBarcodeScanState.Idle, onScan = {}, onSubmit = {})
            DsBarcodeScanField("3017620422003", {}, DsBarcodeScanState.Scanning, onScan = {}, onSubmit = {})
            DsBarcodeScanField("3017620422003", {}, DsBarcodeScanState.Found("Café crème 250 g"), onScan = {}, onSubmit = {})
            DsBarcodeScanField("0000000000000", {}, DsBarcodeScanState.NotFound(), onScan = {}, onSubmit = {})
            DsBarcodeScanField("3017620422003", {}, DsBarcodeScanState.Idle, onScan = {}, onSubmit = {}, enabled = false)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                DsBarcodeScanField("3017620422003", {}, DsBarcodeScanState.Found("Café crème 250 g"), onScan = {}, onSubmit = {})
            }
        }
    }
