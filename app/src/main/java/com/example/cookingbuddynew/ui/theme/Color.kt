package com.example.cookingbuddynew.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val splashScreenBackground = Color(0xFFF5C946)
val lightRed = Color(0xC1FF0000)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)
val gradientStart = Color(0xFFFFE500)
val gradientEnd = Color(0xFFC2BE82)

// yellow gradient

val yellowGradient = Brush.verticalGradient(
    colors = listOf(gradientStart, gradientEnd),
    startY = 0f,
    endY = Float.POSITIVE_INFINITY
)