package com.facephi.demonfc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.view.WindowCompat
import com.facephi.demonfc.R

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8AB4FF),
    secondary = Color(0xFF03DAC5),
    tertiary = Pink80,
    background = Color(0xFF1E1E1E),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2A2A2A),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onSurfaceVariant = Color(0xFFE5E7EB),
    outline = Color(0xFF9CA3AF),
    outlineVariant = Color(0xFF3A3A3A),
    error = Color(0xFFDD3631)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3167FC),
    secondary = Color(0xFF03DAC5),
    tertiary = Pink40,
    background = Color(0xFFFFFBFE),
    surface = Color.White,
    surfaceVariant = Color(0xFFF2F4F8),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1D2C4D),
    onSurface = Color(0xFF1D2C4D),
    onSurfaceVariant = Color(0xFF526080),
    outline = Color(0xFF6B7280),
    outlineVariant = Color(0xFFD6DAE5),
    error = Color(0xFFDD3631)
)

@Composable
fun DemoNFCTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor =
                if (darkTheme) colorScheme.background.toArgb() else colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
val appFontFamily = FontFamily(
    Font(R.font.sdk_font, FontWeight.Normal),
    Font(R.font.sdk_font_light, FontWeight.Light),
    Font(R.font.sdk_font_semibold, FontWeight.SemiBold),
    Font(R.font.sdk_font_bold, FontWeight.ExtraBold)
)
