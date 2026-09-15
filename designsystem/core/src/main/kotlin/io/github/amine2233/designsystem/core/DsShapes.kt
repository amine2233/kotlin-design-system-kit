package io.github.amine2233.designsystem.core

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

object DsShapes {
    val xs = RoundedCornerShape(4.dp)
    val sm = RoundedCornerShape(8.dp)
    val md = RoundedCornerShape(12.dp)
    val lg = RoundedCornerShape(16.dp)
    val xl = RoundedCornerShape(24.dp)
    val pill = RoundedCornerShape(50)
    val sheet = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

    val button = pill
    val chip = pill
    val card = md
    val input = lg
    val key = md
}
