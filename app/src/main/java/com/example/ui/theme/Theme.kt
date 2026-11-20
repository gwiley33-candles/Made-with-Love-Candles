package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PinkFloralDark,
    onPrimary = DeepEspresso,
    primaryContainer = Color(0xFF601B34),
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = Color(0xFFE2A4B8),
    onSecondary = Color(0xFF471A2A),
    secondaryContainer = Color(0xFF5F2E40),
    onSecondaryContainer = Color(0xFFFFD9E4),
    tertiary = BotanicalSage,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF284030),
    onTertiaryContainer = Color(0xFFCEE5D3),
    background = DarkCharcoalBg,
    onBackground = Color(0xFFF7ECF0),
    surface = DarkSurface,
    onSurface = Color(0xFFF7ECF0),
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = Color(0xFFD6B5BF),
    outline = DarkBorder,
    outlineVariant = Color(0xFF422830)
)

private val LightColorScheme = lightColorScheme(
    primary = PinkFloralPrimary,
    onPrimary = Color.White,
    primaryContainer = PinkFloralSoft,
    onPrimaryContainer = Color(0xFF4A0A20),
    secondary = WarmBronze,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFADAE3),
    onSecondaryContainer = Color(0xFF3B0B1D),
    tertiary = BotanicalSage,
    onTertiary = Color.White,
    tertiaryContainer = BotanicalSageLight,
    onTertiaryContainer = Color(0xFF14301B),
    background = CreamBackground,
    onBackground = TextDarkPrimary,
    surface = WarmCardSurface,
    onSurface = TextDarkPrimary,
    surfaceVariant = WarmSurfaceVariant,
    onSurfaceVariant = TextDarkSecondary,
    outline = WarmOutline,
    outlineVariant = Color(0xFFEED7DF)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false to prioritize our signature Pink Floral palette!
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CandleTypography,
        content = content
    )
}
