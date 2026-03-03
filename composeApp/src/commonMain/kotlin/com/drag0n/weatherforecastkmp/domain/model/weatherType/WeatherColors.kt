package com.drag0n.weatherforecastkmp.domain.model.weatherType

import androidx.compose.ui.graphics.Color

data class WeatherColors(
    val gradientStart: Color,
    val gradientEnd: Color,
    val circle1: Color,
    val circle2: Color,
    val circle3: Color,
    val circle4: Color,
    val circle5: Color,
    val textColor: Color,
    val textColorSecondary: Color,
    val iconTint: Color,
    val detailIconTint1: Color,
    val detailIconTint2: Color,
    val detailIconTint3: Color,
    val sunIconTint: Color,
    val sunsetIconTint: Color,
    val cardColor: Color,
    val surfaceColor: Color,
    val buttonColor: Color,
    val buttonTextColor: Color,
    val buttonColorSecondary: Color,
    val buttonTextColorSecondary: Color,
    val dividerColor: Color
){
    companion object {
        val Default = WeatherColors(
            gradientStart = Color(0xFF1A237E), // Темно-синий
            gradientEnd = Color(0xFF121212),   // Почти черный
            circle1 = Color(0xFF3F51B5),       // Индиго
            circle2 = Color(0xFF303F9F),
            circle3 = Color(0xFF283593),
            circle4 = Color(0xFF1A237E),
            circle5 = Color(0xFF0D47A1),
            textColor = Color.White,
            textColorSecondary = Color.White.copy(alpha = 0.7f),
            iconTint = Color.White,
            detailIconTint1 = Color(0xFF81D4FA),
            detailIconTint2 = Color(0xFFB3E5FC),
            detailIconTint3 = Color(0xFFE1F5FE),
            sunIconTint = Color(0xFFFFD600),
            sunsetIconTint = Color(0xFFFFAB40),
            cardColor = Color.White.copy(alpha = 0.1f),
            surfaceColor = Color(0xFF121212),
            buttonColor = Color(0xFF3F51B5),
            buttonTextColor = Color.White,
            buttonColorSecondary = Color.White.copy(alpha = 0.2f),
            buttonTextColorSecondary = Color.White,
            dividerColor = Color.White.copy(alpha = 0.12f)
        )
    }
}

