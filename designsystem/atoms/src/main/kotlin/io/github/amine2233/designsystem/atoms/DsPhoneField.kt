package io.github.amine2233.designsystem.atoms

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsIcons
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme

/** French mobile grouping: 06 12 34 56 78. */
val DsPhoneGroupsFr: List<Int> = listOf(2, 2, 2, 2, 2)

/**
 * Groups [digits] for display: "0612345678" → "06 12 34 56 78".
 * Digits past the last group are appended as-is rather than dropped, so a pasted international
 * number stays visible instead of silently losing its tail.
 */
fun dsFormatPhone(
    digits: String,
    groups: List<Int> = DsPhoneGroupsFr,
): String {
    val builder = StringBuilder()
    var index = 0
    for (size in groups) {
        if (index >= digits.length) break
        if (builder.isNotEmpty()) builder.append(' ')
        builder.append(digits, index, minOf(index + size, digits.length))
        index += size
    }
    if (index < digits.length) builder.append(digits, index, digits.length)
    return builder.toString()
}

private class PhoneTransformation(
    private val groups: List<Int>,
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = dsFormatPhone(text.text, groups)
        val mapping =
            object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    if (offset <= 0) return 0
                    var digitsSeen = 0
                    formatted.forEachIndexed { index, char ->
                        if (char.isDigit()) {
                            digitsSeen++
                            if (digitsSeen == offset) return index + 1
                        }
                    }
                    return formatted.length
                }

                override fun transformedToOriginal(offset: Int): Int = formatted.take(offset).count(Char::isDigit)
            }
        return TransformedText(AnnotatedString(formatted), mapping)
    }
}

/**
 * Phone entry that displays grouped digits while the value stays raw digits.
 *
 * The caller never receives spaces, so nothing downstream has to strip them before dialling or
 * storing; the grouping is display only. Non-digits are dropped on input — a keyboard that offers
 * "+", "*" and "#" would otherwise let them through.
 */
@Composable
fun DsPhoneField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "06 12 34 56 78",
    groups: List<Int> = DsPhoneGroupsFr,
    isError: Boolean = false,
    supportingText: String? = null,
    required: Boolean = false,
    enabled: Boolean = true,
) {
    DsTextField(
        value = value,
        onValueChange = { input -> onValueChange(input.filter(Char::isDigit).take(groups.sum())) },
        modifier = modifier,
        label = label,
        placeholder = placeholder,
        leadingIcon = DsIcons.Phone,
        isError = isError,
        supportingText = supportingText,
        required = required,
        enabled = enabled,
        visualTransformation = PhoneTransformation(groups),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
    )
}

@DsComponentPreview
@Composable
private fun DsPhoneFieldPreview() =
    DsPreview {
        DsPhoneField("0612345678", {}, label = "Téléphone du client")
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsPhoneField("", {}, label = "Téléphone", required = true)
        Spacer(Modifier.height(DsTheme.spacing.sm))
        DsPhoneField("0612", {}, label = "Incomplet", isError = true, supportingText = "10 chiffres attendus")
    }
