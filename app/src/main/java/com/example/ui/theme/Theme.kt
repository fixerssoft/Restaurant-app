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
    primary = CrimsonRedPrimaryDark,
    onPrimary = CharcoalDark,
    primaryContainer = CrimsonRedDark,
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = SaffronOrange,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF5D2B00),
    onSecondaryContainer = AmberLight,
    tertiary = GoldenAmber,
    onTertiary = Color.Black,
    background = CharcoalBackgroundDark,
    onBackground = CreamTextDark,
    surface = CharcoalSurfaceDark,
    onSurface = CreamTextDark,
    surfaceVariant = CharcoalSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFD7CCC8)
)

private val LightColorScheme = lightColorScheme(
    primary = CrimsonRedPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEBEE),
    onPrimaryContainer = CrimsonRedDark,
    secondary = SaffronOrange,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = Color(0xFFBF360C),
    tertiary = GoldenAmber,
    onTertiary = CharcoalDark,
    background = CreamBackground,
    onBackground = CharcoalDark,
    surface = CreamSurface,
    onSurface = CharcoalDark,
    surfaceVariant = CreamSurfaceVariant,
    onSurfaceVariant = CharcoalMedium
)

@Composable
fun BabaJiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
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
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    BabaJiTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
