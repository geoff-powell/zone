package com.greenmiststudios.zone.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ZoneIndigo = Color(0xFF5B5FE8)
val ZoneIndigoDark = Color(0xFF3D41C8)
val ZoneIndigoContainer = Color(0xFFEEEEFF)
val ZonePurple = Color(0xFF7C3AED)
val ZoneBackground = Color(0xFFF8F7FF)
val ZoneSurface = Color(0xFFFFFFFF)

val PriorityHigh = Color(0xFFEF4444)
val PriorityHighContainer = Color(0xFFFFEDED)
val PriorityHighContainerDark = Color(0xFF3D1515)
val PriorityMedium = Color(0xFFF59E0B)
val PriorityMediumContainer = Color(0xFFFFF8E1)
val PriorityMediumContainerDark = Color(0xFF3D2E00)
val PriorityLow = Color(0xFF10B981)
val PriorityLowContainer = Color(0xFFECFDF5)
val PriorityLowContainerDark = Color(0xFF0D2E1F)
val PriorityCompleted = Color(0xFF9CA3AF)
val PriorityCompletedContainer = Color(0xFFF3F4F6)
val PriorityCompletedContainerDark = Color(0xFF1F2023)

private val ZoneLightColorScheme =
  lightColorScheme(
    primary = ZoneIndigo,
    onPrimary = Color.White,
    primaryContainer = ZoneIndigoContainer,
    onPrimaryContainer = ZoneIndigoDark,
    secondary = ZonePurple,
    onSecondary = Color.White,
    background = ZoneBackground,
    onBackground = Color(0xFF1A1A2E),
    surface = ZoneSurface,
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = Color(0xFFF0EFFF),
    onSurfaceVariant = Color(0xFF4B4B6B),
    outline = Color(0xFFD0CEF0),
    error = PriorityHigh
  )

private val ZoneDarkColorScheme =
  darkColorScheme(
    primary = Color(0xFF8B8FFF),
    onPrimary = Color(0xFF1A1D6E),
    primaryContainer = Color(0xFF2D3092),
    onPrimaryContainer = Color(0xFFBDBFFF),
    secondary = Color(0xFFBB86FC),
    onSecondary = Color(0xFF1A0050),
    background = Color(0xFF0F0F1A),
    onBackground = Color(0xFFE8E8F5),
    surface = Color(0xFF1A1A2E),
    onSurface = Color(0xFFE8E8F5),
    surfaceVariant = Color(0xFF252538),
    onSurfaceVariant = Color(0xFFAAAAAC),
    outline = Color(0xFF3D3D5C),
    error = Color(0xFFFF7575)
  )

@Composable
fun ZoneTheme(content: @Composable () -> Unit) {
  val isDark = isSystemInDarkTheme()
  MaterialTheme(
    colorScheme = if (isDark) ZoneDarkColorScheme else ZoneLightColorScheme,
    content = content
  )
}
