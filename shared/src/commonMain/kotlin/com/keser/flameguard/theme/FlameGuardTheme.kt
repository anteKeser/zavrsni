package com.keser.flameguard.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.keser.flameguard.data.SettingsRepository
import org.koin.compose.koinInject

private val DarkColorScheme = darkColorScheme(
    primary = FlameOrange,
    onPrimary = TextPrimary,
    secondary = FlameOrangeDark,
    background = BgDarkBase,
    surface = CardBgGlass,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = StatusDanger
)

private val LightColorScheme = lightColorScheme(
    primary = FlameOrange,
    onPrimary = TextPrimary,
    secondary = FlameOrangeDark,
    background = BgLightBase,
    surface = CardBgLight,
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight,
    error = StatusDanger
)

@Composable
fun FlameGuardTheme(
    content: @Composable () -> Unit
) {
    val settingsRepository = koinInject<SettingsRepository>()
    val isDarkMode by settingsRepository.isDarkMode.collectAsState()

    MaterialTheme(
        colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme,
        typography = AppTypography,
        content = content
    )
}
