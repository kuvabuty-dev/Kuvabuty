package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VaultColorScheme = darkColorScheme(
  primary = CyberPrimary,
  onPrimary = CyberOnPrimary,
  primaryContainer = CyberPrimaryContainer,
  onPrimaryContainer = CyberPrimary,
  secondary = CyberSecondary,
  onSecondary = CyberOnSecondary,
  secondaryContainer = CyberSecondaryContainer,
  onSecondaryContainer = CyberSecondary,
  tertiary = CyberTertiary,
  tertiaryContainer = CyberTertiaryContainer,
  background = CyberDarkBackground,
  onBackground = CyberTextPrimary,
  surface = CyberDarkSurface,
  onSurface = CyberTextPrimary,
  surfaceVariant = CyberDarkSurfaceVariant,
  onSurfaceVariant = CyberTextSecondary,
  outline = CyberBorder,
  outlineVariant = CyberBorderLight,
  error = CyberAccentRed
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = VaultColorScheme,
    typography = Typography,
    content = content
  )
}
