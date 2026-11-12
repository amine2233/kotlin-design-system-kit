package io.github.amine2233.designsystem.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import io.github.amine2233.designsystem.core.DsColorTheme
import io.github.amine2233.designsystem.core.DsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            var colorTheme by rememberSaveable { mutableStateOf(DsColorTheme.Teal) }
            DsTheme(colorTheme = colorTheme) { SampleApp(colorTheme, onColorThemeChange = { colorTheme = it }) }
        }
    }
}
