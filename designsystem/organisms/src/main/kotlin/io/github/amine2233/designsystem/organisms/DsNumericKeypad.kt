package io.github.amine2233.designsystem.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.amine2233.designsystem.core.DsComponentPreview
import io.github.amine2233.designsystem.core.DsPreview
import io.github.amine2233.designsystem.core.DsTheme
import io.github.amine2233.designsystem.molecules.DsKeyTone
import io.github.amine2233.designsystem.molecules.DsKeypadKey

sealed interface DsKey {
    data class Digit(val value: Char) : DsKey
    data object Comma : DsKey
    data object Backspace : DsKey
}

/** Shared 3×4 keypad: 1–9, comma, 0, backspace (red). Rows share height via [rowModifier] (e.g. weight(1f)). */
@Composable
fun DsNumericKeypad(
    onKey: (DsKey) -> Unit,
    modifier: Modifier = Modifier,
    rowModifier: Modifier = Modifier,
    gap: androidx.compose.ui.unit.Dp = 7.dp,
) {
    val rows = listOf(
        listOf(DsKey.Digit('1'), DsKey.Digit('2'), DsKey.Digit('3')),
        listOf(DsKey.Digit('4'), DsKey.Digit('5'), DsKey.Digit('6')),
        listOf(DsKey.Digit('7'), DsKey.Digit('8'), DsKey.Digit('9')),
        listOf(DsKey.Comma, DsKey.Digit('0'), DsKey.Backspace),
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(gap)) {
        rows.forEach { row ->
            Row(Modifier.fillMaxWidth().then(rowModifier), horizontalArrangement = Arrangement.spacedBy(gap)) {
                row.forEach { key ->
                    val (label, tone) = when (key) {
                        is DsKey.Digit -> key.value.toString() to DsKeyTone.Default
                        DsKey.Comma -> "," to DsKeyTone.Secondary
                        DsKey.Backspace -> "⌫" to DsKeyTone.Destructive
                    }
                    DsKeypadKey(label, onClick = { onKey(key) }, modifier = Modifier.weight(1f).fillMaxHeight(), tone = tone)
                }
            }
        }
    }
}

/** Applies a key press to a French-formatted decimal string ("1 500,00"). Digits are typed right-to-left in cents. */
fun String.applyKey(key: DsKey): String {
    val digits = filter { it.isDigit() }
    val next = when (key) {
        is DsKey.Digit -> (digits + key.value).trimStart('0')
        DsKey.Backspace -> digits.dropLast(1)
        DsKey.Comma -> digits
    }
    val cents = next.padStart(3, '0')
    val intPart = cents.dropLast(2).reversed().chunked(3).joinToString(" ").reversed()
    return "$intPart,${cents.takeLast(2)}"
}

@DsComponentPreview
@Composable
private fun DsNumericKeypadPreview() = DsPreview {
    DsNumericKeypad(onKey = {}, rowModifier = Modifier.height(52.dp))
}
