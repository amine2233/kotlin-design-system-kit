package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import io.github.amine2233.designsystem.atoms.DsButton
import io.github.amine2233.designsystem.atoms.DsErrorText
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.atoms.DsIconButton
import io.github.amine2233.designsystem.atoms.DsTag
import io.github.amine2233.designsystem.atoms.DsTagTone
import io.github.amine2233.designsystem.atoms.DsText
import io.github.amine2233.designsystem.atoms.DsTextField
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** What the caller decided about the code typed in a [DsPromoCodeField]; the field never validates. */
@Immutable
sealed interface DsPromoCodeState {
    /** Nothing applied yet: the code can be typed and applied. */
    data object Idle : DsPromoCodeState

    /** [code] is applied and gives [description] ("−10 %"). */
    data class Applied(
        val code: String,
        val description: String,
    ) : DsPromoCodeState

    /** The code was refused with [message]; it stays editable. */
    data class Invalid(
        val message: String,
    ) : DsPromoCodeState
}

/** The default French description of an applied code: "Code PROMO10 appliqué, −10 %". */
fun dsPromoCodeAppliedDescription(
    code: String,
    description: String,
): String = "Code $code appliqué, $description"

/** The default French description of the remove action: "Retirer le code PROMO10". */
fun dsPromoCodeRemoveDescription(code: String): String = "Retirer le code $code"

/**
 * A promo code entry: a text field with an apply button, which turns into the applied code with a
 * remove action once the caller reports it as [DsPromoCodeState.Applied].
 *
 * The apply button stays disabled while [code] is blank. [DsPromoCodeState.Invalid] marks the field in
 * error and announces its message; the applied row is announced and read as one description built by
 * [appliedDescription].
 */
@Composable
fun DsPromoCodeField(
    code: String,
    onCodeChange: (String) -> Unit,
    state: DsPromoCodeState,
    onApply: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Code promo",
    placeholder: String = "Saisir un code",
    applyText: String = "Appliquer",
    appliedDescription: (code: String, description: String) -> String = ::dsPromoCodeAppliedDescription,
    removeDescription: (code: String) -> String = ::dsPromoCodeRemoveDescription,
) {
    DsFormField(modifier = modifier.fillMaxWidth(), label = label) {
        when (state) {
            is DsPromoCodeState.Applied -> {
                AppliedRow(state, appliedDescription, removeDescription, onRemove)
            }

            else -> {
                Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.xs)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        DsTextField(
                            value = code,
                            onValueChange = onCodeChange,
                            modifier = Modifier.weight(1f),
                            placeholder = placeholder,
                            isError = state is DsPromoCodeState.Invalid,
                        )
                        DsButton(applyText, onClick = onApply, enabled = code.isNotBlank())
                    }
                    if (state is DsPromoCodeState.Invalid) {
                        DsErrorText(state.message, Modifier.semantics { liveRegion = LiveRegionMode.Polite })
                    }
                }
            }
        }
    }
}

@Composable
private fun AppliedRow(
    state: DsPromoCodeState.Applied,
    appliedDescription: (code: String, description: String) -> String,
    removeDescription: (code: String) -> String,
    onRemove: () -> Unit,
) {
    val c = DsTheme.colors
    val description = appliedDescription(state.code, state.description)
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .heightIn(min = DsTheme.spacing.minTouchTarget)
                .background(c.successContainer, DsTheme.shapes.input)
                .padding(start = DsTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier =
                Modifier
                    .weight(1f)
                    .clearAndSetSemantics {
                        contentDescription = description
                        liveRegion = LiveRegionMode.Polite
                    },
            horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(DsIcons.Success, contentDescription = null, tint = c.success, modifier = Modifier.size(DsTheme.spacing.mlg))
            DsText(state.code, style = DsTheme.typography.bodyStrong, color = c.textPrimary, maxLines = 1)
            DsTag(state.description, tone = DsTagTone.Success)
        }
        DsIconButton(DsIcons.Close, contentDescription = removeDescription(state.code), onClick = onRemove)
    }
}

@DsComponentPreview
@Composable
private fun DsPromoCodeFieldPreview() =
    DsPreview {
        Column(verticalArrangement = Arrangement.spacedBy(DsTheme.spacing.md)) {
            DsPromoCodeField("", {}, DsPromoCodeState.Idle, onApply = {}, onRemove = {})
            DsPromoCodeField("PROMO10", {}, DsPromoCodeState.Idle, onApply = {}, onRemove = {})
            DsPromoCodeField("PROMO10", {}, DsPromoCodeState.Applied("PROMO10", "−10 %"), onApply = {}, onRemove = {})
            DsPromoCodeField("NOEL2019", {}, DsPromoCodeState.Invalid("Code expiré"), onApply = {}, onRemove = {})
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                DsPromoCodeField("PROMO10", {}, DsPromoCodeState.Applied("PROMO10", "−10 %"), onApply = {}, onRemove = {})
            }
        }
    }
