package com.greenmiststudios.zone.ui

import androidx.compose.material3.MaterialTheme
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
val PriorityMedium = Color(0xFFF59E0B)
val PriorityMediumContainer = Color(0xFFFFF8E1)
val PriorityLow = Color(0xFF10B981)
val PriorityLowContainer = Color(0xFFECFDF5)
val PriorityCompleted = Color(0xFF9CA3AF)
val PriorityCompletedContainer = Color(0xFFF3F4F6)

private val ZoneColorScheme =
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
    error = PriorityHigh,
  )

@Composable
fun ZoneTheme(content: @Composable () -> Unit) {
  MaterialTheme(colorScheme = ZoneColorScheme, content = content)
}
