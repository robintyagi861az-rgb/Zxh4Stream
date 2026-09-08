package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = NetflixRed,
  onPrimary = Color.White,
  primaryContainer = NetflixRedDark,
  onPrimaryContainer = Color.White,
  secondary = NetflixRedLight,
  onSecondary = Color.White,
  tertiary = GoldStar,
  background = BackgroundBlack,
  onBackground = TextPrimary,
  surface = SurfaceBlack,
  onSurface = TextPrimary,
  surfaceVariant = SurfaceCard,
  onSurfaceVariant = TextSecondary,
  outline = BorderGray
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = DarkColorScheme,
    typography = Typography,
    content = content
  )
}
