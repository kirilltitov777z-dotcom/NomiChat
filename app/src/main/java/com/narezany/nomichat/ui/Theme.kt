package com.narezany.nomichat.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NomiBlue = Color(0xFF3390EC)
val NomiBlueDark = Color(0xFF2277CC)
val NomiBg = Color(0xFFFFFFFF)
val NomiSoftBg = Color(0xFFF4F6F9)
val OutBubble = Color(0xFFE1FFC7)
val InBubble = Color(0xFFFFFFFF)
val TextMain = Color(0xFF1C1C1E)
val TextGray = Color(0xFF8B9099)
val LineColor = Color(0xFFECEEF1)
val GiftPink = Color(0xFFFF6B8B)

private val scheme = lightColorScheme(
    primary = NomiBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCEEFF),
    onPrimaryContainer = Color(0xFF124D8A),
    secondary = GiftPink,
    onSecondary = Color.White,
    background = NomiBg,
    onBackground = TextMain,
    surface = NomiBg,
    onSurface = TextMain,
    surfaceVariant = NomiSoftBg,
    onSurfaceVariant = TextGray,
    outline = LineColor,
)

private val type = Typography(
    titleLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 17.sp),
    titleSmall = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 16.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 14.sp),
    bodySmall = TextStyle(fontFamily = FontFamily.SansSerif, fontSize = 12.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.SemiBold, fontSize = 14.sp),
    labelMedium = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Medium, fontSize = 12.sp),
)

@Composable
fun NomiTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, typography = type, content = content)
}
