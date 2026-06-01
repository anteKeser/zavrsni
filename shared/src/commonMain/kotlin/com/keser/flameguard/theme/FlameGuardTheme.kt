package com.keser.flameguard.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

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

@Composable
fun FlameGuardTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}