package io.github.amine2233.designsystem.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.atoms.DsFormField
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsShapes
import io.github.amine2233.designsystem.core.DsTheme

/** Digits only, never longer than the field: the whole input contract, kept testable. */
internal fun otpDigits(
    input: String,
    length: Int,
): String = input.filter { it.isDigit() }.take(length)

/**
 * Fixed-length code entry — the 2FA code after an OAuth sign-in, a manager override PIN.
 *
 * One hidden text field drives [length] boxes, so the platform keyboard, paste and autofill of a
 * one-time code keep working; per-box fields would break all three. Non-digits are dropped and
 * [onFilled] fires once the last digit lands, which is where callers submit.
 */
@Composable
fun DsOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    label: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    obscure: Boolean = false,
    onFilled: (String) -> Unit = {},
) {
    val c = DsTheme.colors
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    DsFormField(modifier = modifier, label = label, supportingText = supportingText, isError = isError, enabled = enabled) {
        BasicTextField(
            value = value,
            onValueChange = { input ->
                val digits = otpDigits(input, length)
                if (digits != value) {
                    onValueChange(digits)
                    if (digits.length == length) onFilled(digits)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword, imeAction = ImeAction.Done),
            textStyle = TextStyle(color = Color.Transparent),
            cursorBrush = SolidColor(Color.Transparent),
            decorationBox = {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(DsTheme.spacing.sm)) {
                    repeat(length) { index ->
                        val char = value.getOrNull(index)
                        val active = enabled && focused && index == value.length.coerceAtMost(length - 1)
                        Box(
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .height(56.dp)
                                    .clip(DsShapes.sm)
                                    .background(if (enabled) c.surfaceSubtle else c.surfaceMuted)
                                    .border(
                                        width = if (active) 2.dp else 1.dp,
                                        color =
                                            if (isError) {
                                                c.error
                                            } else if (active) {
                                                c.primary
                                            } else {
                                                c.border
                                            },
                                        shape = DsShapes.sm,
                                    ),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (char != null) {
                                Text(
                                    if (obscure) "•" else char.toString(),
                                    style = DsTheme.typography.titleLarge,
                                    color = if (enabled) c.textPrimary else c.textDisabled,
                                )
                            }
                        }
                    }
                }
            },
        )
    }
}

@DsComponentPreview
@Composable
private fun DsOtpFieldPreview() =
    DsPreview {
        DsOtpField("4218", {}, label = "Code reçu par SMS")
        DsOtpField("1234", {}, length = 4, obscure = true, isError = true, supportingText = "Code incorrect")
    }
