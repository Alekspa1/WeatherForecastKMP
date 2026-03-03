package com.drag0n.weatherforecastkmp.presentation.others

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherColors
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherType
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin



@Composable
fun BoxBackgroundCircle(weatherColors: WeatherColors){

    val infiniteTransition = rememberInfiniteTransition()
    val c1 by animateColorAsState(weatherColors.circle1, tween(1000))
    val c2 by animateColorAsState(weatherColors.circle2, tween(1000))
    val c3 by animateColorAsState(weatherColors.circle3, tween(1000))
    val c4 by animateColorAsState(weatherColors.circle4, tween(1000))
    val c5 by animateColorAsState(weatherColors.circle5, tween(1000))

    val bgStart by animateColorAsState(weatherColors.gradientStart, tween(1000))
    val bgEnd by animateColorAsState(weatherColors.gradientEnd, tween(1000))

    // Круг 1 - движется вверх-вниз
    val offsetY1 by infiniteTransition.animateFloat(
        initialValue = -80f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val offsetX1 by infiniteTransition.animateFloat(
        initialValue = -50f,
        targetValue = 50f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Круг 2 - движется по кругу
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Круг 3 - пульсирует размером
    val scale2 by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Круг 3 - движется по диагонали
    val offsetX3 by infiniteTransition.animateFloat(
        initialValue = -60f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val offsetY3 by infiniteTransition.animateFloat(
        initialValue = -60f,
        targetValue = 60f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Круг 4 - медленно движется
    val offsetX4 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 150f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val offsetY4 by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val radius = 100f
    val offsetX2 = radius * cos(angle * (PI.toFloat() / 180f))
    val offsetY2 = radius * sin(angle * (PI.toFloat() / 180f))

    Canvas(modifier = Modifier.fillMaxSize().background(
        Brush.verticalGradient(
            colors = listOf(
                bgStart.copy(alpha = 0.5f), // Используем твой цвет из WeatherColors
                bgEnd.copy(alpha = 0.3f),   // Плавный переход к концу градиента
                Color.Transparent                     // Полная прозрачность снизу
            )
        )

    )
    ) {
        val width = size.width
        val height = size.height

        // Функция-помощник для рисования "пушистого" круга
        fun drawBlurryCircle(center: Offset, radius: Float, color: Color) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.45f), color.copy(alpha = 0.05f), Color.Transparent),
                    center = center,
                    radius = radius
                ),
                center = center,
                radius = radius
            )
        }

        // 1. Круг вверх-вниз
        drawBlurryCircle(
            center = Offset(width * 0.2f + offsetX1.dp.toPx(), height * 0.3f + offsetY1.dp.toPx()),
            radius = 200.dp.toPx(),
            color = c1
        )

        // 2. Круг по траектории (используем твои offsetX2/offsetY2)
        drawBlurryCircle(
            center = Offset(width * 0.5f + offsetX2.dp.toPx(), height * 0.5f + offsetY2.dp.toPx()),
            radius = 180.dp.toPx(),
            color = c2
        )

        // 3. Пульсирующий круг
        drawBlurryCircle(
            center = Offset(width * 0.8f + offsetX3.dp.toPx(), height * 0.2f + offsetY3.dp.toPx()),
            radius = (100 * scale2).dp.toPx(),
            color = c3
        )

        // 4. Медленный большой круг
        drawBlurryCircle(
            center = Offset(width * 0.1f + offsetX4.dp.toPx(), height * 0.8f + offsetY4.dp.toPx()),
            radius = 220.dp.toPx(),
            color = c4
        )

        // 5. Статичный акцент
        drawBlurryCircle(
            center = Offset(width * 0.9f, height * 0.1f),
            radius = 150.dp.toPx(),
            color = c5
        )
    }
}

fun getWeatherColors(weatherType: WeatherType, isDayTime: Boolean): WeatherColors {
    return when (weatherType) {
        WeatherType.SUNNY -> if (isDayTime) sunnyDayColors else sunnyNightColors
        WeatherType.CLOUDY -> if (isDayTime) cloudyDayColors else cloudyNightColors
        WeatherType.RAINY -> if (isDayTime) rainyDayColors else rainyNightColors
        WeatherType.SNOWY -> if (isDayTime) snowyDayColors else snowyNightColors
        WeatherType.STORMY -> if (isDayTime) stormyDayColors else stormyNightColors
        WeatherType.FOGGY -> if (isDayTime) foggyDayColors else foggyNightColors
        WeatherType.Overcast -> if (isDayTime) overcastDayColors else overcastNightColors
        WeatherType.Drizzle -> if (isDayTime) drizzleDayColors else drizzleNightColors
    }
}

val sunnyDayColors = WeatherColors(
    gradientStart = Color(0xFF4A90E2),
    gradientEnd = Color(0xFF87CEEB),
    circle1 = Color(0xFFFFD700),
    circle2 = Color(0xFF87CEEB),
    circle3 = Color(0xFFFFA07A),
    circle4 = Color(0xFF98FB98),
    circle5 = Color(0xFFFFB6C1),
    textColor = Color.White,
    textColorSecondary = Color(0xFF4A6A8A),
    iconTint = Color(0xFFFFD700),
    detailIconTint1 = Color(0xFF4A90E2),
    detailIconTint2 = Color(0xFF20B2AA),
    detailIconTint3 = Color(0xFFE57373),
    sunIconTint = Color(0xFFFFB100),
    sunsetIconTint = Color(0xFFFF7F50),
    cardColor = Color.White.copy(alpha = 0.3f),
    surfaceColor = Color(0xFF4A90E2).copy(alpha = 0.2f),
    buttonColor = Color(0xFF4A90E2),
    buttonTextColor = Color.White,
    buttonColorSecondary = Color(0xFFFFD700),
    buttonTextColorSecondary = Color(0xFF1A2F3F),
    dividerColor = Color(0xFF4A6A8A).copy(alpha = 0.3f)
)

val cloudyDayColors = WeatherColors(
    gradientStart = Color(0xFF757F9A),
    gradientEnd = Color(0xFFD7DDE8),
    circle1 = Color(0xFFB0C4DE),
    circle2 = Color(0xFF778899),
    circle3 = Color(0xFFC0C0C0),
    circle4 = Color(0xFFA9A9A9),
    circle5 = Color(0xFFD3D3D3),
    textColor = Color(0xFF2F4F4F),
    textColorSecondary = Color(0xFF708090),
    iconTint = Color(0xFFA9A9A9),
    detailIconTint1 = Color(0xFF4682B4),
    detailIconTint2 = Color(0xFF5F9EA0),
    detailIconTint3 = Color(0xFFCD5C5C),
    sunIconTint = Color(0xFFB0C4DE),
    sunsetIconTint = Color(0xFFBC8F8F),
    cardColor = Color.White.copy(alpha = 0.25f),
    surfaceColor = Color(0xFF757F9A).copy(alpha = 0.2f),
    buttonColor = Color(0xFF4682B4),
    buttonTextColor = Color.White,
    buttonColorSecondary = Color(0xFF778899),
    buttonTextColorSecondary = Color.White,
    dividerColor = Color(0xFF708090).copy(alpha = 0.3f)
)

val rainyDayColors = WeatherColors(
    gradientStart = Color(0xFF4B79A1),
    gradientEnd = Color(0xFF283E51),
    circle1 = Color(0xFF5F9EA0),
    circle2 = Color(0xFF4682B4),
    circle3 = Color(0xFF6A5ACD),
    circle4 = Color(0xFF7B68EE),
    circle5 = Color(0xFF4169E1),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f),
    iconTint = Color(0xFF87CEEB),
    detailIconTint1 = Color(0xFF87CEEB),
    detailIconTint2 = Color(0xFF20B2AA),
    detailIconTint3 = Color(0xFFCD5C5C),
    sunIconTint = Color(0xFF87CEEB),
    sunsetIconTint = Color(0xFFB0C4DE),
    cardColor = Color.White.copy(alpha = 0.15f),
    surfaceColor = Color.White.copy(alpha = 0.15f),
    buttonColor = Color.White.copy(alpha = 0.2f),
    buttonTextColor = Color.White,
    buttonColorSecondary = Color.White.copy(alpha = 0.2f),
    buttonTextColorSecondary = Color.White,
    dividerColor = Color.White.copy(alpha = 0.2f)
)

val snowyDayColors = WeatherColors(
    gradientStart = Color(0xFFE0EAF0),
    gradientEnd = Color(0xFFF0F5FA),
    circle1 = Color.White,
    circle2 = Color(0xFFE8F0F8),
    circle3 = Color(0xFFD0E0F0),
    circle4 = Color(0xFFB8D0E8),
    circle5 = Color(0xFFA0C0E0),
    textColor = Color(0xFF1A2F3F),
    textColorSecondary = Color(0xFF4A6A8A),
    iconTint = Color.White,
    detailIconTint1 = Color(0xFF4682B4),
    detailIconTint2 = Color(0xFF5F9EA0),
    detailIconTint3 = Color(0xFFCD5C5C),
    sunIconTint = Color(0xFFFFD700),
    sunsetIconTint = Color(0xFFFFA07A),
    cardColor = Color.White.copy(alpha = 0.4f),
    surfaceColor = Color.White.copy(alpha = 0.3f),
    buttonColor = Color(0xFF4682B4),
    buttonTextColor = Color.White,
    buttonColorSecondary = Color(0xFF5F9EA0),
    buttonTextColorSecondary = Color.White,
    dividerColor = Color(0xFF4A6A8A).copy(alpha = 0.2f)
)

val stormyDayColors = WeatherColors(
    gradientStart = Color(0xFF232526),
    gradientEnd = Color(0xFF414345),
    circle1 = Color(0xFF4A4A4A),
    circle2 = Color(0xFF6B6B6B),
    circle3 = Color(0xFF8B8B8B),
    circle4 = Color(0xFF2C3E50),
    circle5 = Color(0xFF34495E),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f),
    iconTint = Color(0xFFF0E68C),
    detailIconTint1 = Color(0xFF87CEEB),
    detailIconTint2 = Color(0xFF20B2AA),
    detailIconTint3 = Color(0xFFCD5C5C),
    sunIconTint = Color(0xFFF0E68C),
    sunsetIconTint = Color(0xFFDAA520),
    cardColor = Color.White.copy(alpha = 0.1f),
    surfaceColor = Color.White.copy(alpha = 0.1f),
    buttonColor = Color.White.copy(alpha = 0.2f),
    buttonTextColor = Color.White,
    buttonColorSecondary = Color(0xFFF0E68C).copy(alpha = 0.3f),
    buttonTextColorSecondary = Color.White,
    dividerColor = Color.White.copy(alpha = 0.2f)
)

val foggyDayColors = WeatherColors(
    gradientStart = Color(0xFFBFC9CE),
    gradientEnd = Color(0xFFE0E6EB),
    circle1 = Color(0xFFC0C0C0),
    circle2 = Color(0xFFD3D3D3),
    circle3 = Color(0xFFE0E0E0),
    circle4 = Color(0xFFF0F0F0),
    circle5 = Color(0xFFF8F8F8),
    textColor = Color(0xFF2F4F4F),
    textColorSecondary = Color(0xFF708090),
    iconTint = Color(0xFFA9A9A9),
    detailIconTint1 = Color(0xFF4682B4),
    detailIconTint2 = Color(0xFF5F9EA0),
    detailIconTint3 = Color(0xFFCD5C5C),
    sunIconTint = Color(0xFFB0C4DE),
    sunsetIconTint = Color(0xFFBC8F8F),
    cardColor = Color.White.copy(alpha = 0.35f),
    surfaceColor = Color.White.copy(alpha = 0.25f),
    buttonColor = Color(0xFF708090),
    buttonTextColor = Color.White,
    buttonColorSecondary = Color(0xFF808080),
    buttonTextColorSecondary = Color.White,
    dividerColor = Color(0xFF708090).copy(alpha = 0.3f)
)

// Ночные версии цветов
val sunnyNightColors = sunnyDayColors.copy(
    gradientStart = Color(0xFF0A1929),
    gradientEnd = Color(0xFF1A3A5F),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f),
    buttonTextColorSecondary = Color.White
)

val cloudyNightColors = cloudyDayColors.copy(
    gradientStart = Color(0xFF2C3E50),
    gradientEnd = Color(0xFF1A2530),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f),
    buttonTextColor = Color.White,
    buttonTextColorSecondary = Color.White
)

val rainyNightColors = rainyDayColors.copy(
    gradientStart = Color(0xFF0F2027),
    gradientEnd = Color(0xFF203A43),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f)
)

val snowyNightColors = snowyDayColors.copy(
    gradientStart = Color(0xFF2C3E50),
    gradientEnd = Color(0xFF3498DB),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f),
    iconTint = Color.White,
    sunIconTint = Color(0xFFF0E68C),
    sunsetIconTint = Color(0xFFDAA520)
)

val stormyNightColors = stormyDayColors.copy(
    gradientStart = Color(0xFF0B0C10),
    gradientEnd = Color(0xFF1A1F2E),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f)
)

val foggyNightColors = foggyDayColors.copy(
    gradientStart = Color(0xFF353F4F),
    gradientEnd = Color(0xFF4A5568),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f)
)
val overcastDayColors = WeatherColors(
    gradientStart = Color(0xFF949FB0),
    gradientEnd = Color(0xFFD1D9E6),
    circle1 = Color(0xFFAAB5C3),
    circle2 = Color(0xFFB8C1CD),
    circle3 = Color(0xFFC4CDD8),
    circle4 = Color(0xFFD2D9E2),
    circle5 = Color(0xFFDEE4EB),
    textColor = Color(0xFF37474F),
    textColorSecondary = Color(0xFF546E7A),
    iconTint = Color(0xFF78909C),
    detailIconTint1 = Color(0xFF607D8B),
    detailIconTint2 = Color(0xFF90A4AE),
    detailIconTint3 = Color(0xFFB0BEC5),
    sunIconTint = Color(0xFFCFD8DC),
    sunsetIconTint = Color(0xFFB0BEC5),
    cardColor = Color.White.copy(alpha = 0.3f),
    surfaceColor = Color(0xFF949FB0).copy(alpha = 0.15f),
    buttonColor = Color(0xFF78909C),
    buttonTextColor = Color.White,
    buttonColorSecondary = Color(0xFFB0BEC5),
    buttonTextColorSecondary = Color(0xFF37474F),
    dividerColor = Color(0xFFCFD8DC)
)

val overcastNightColors = overcastDayColors.copy(
    gradientStart = Color(0xFF1E272E),
    gradientEnd = Color(0xFF2F3640),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f),
    buttonTextColorSecondary = Color.White
)
val drizzleDayColors = WeatherColors(
    gradientStart = Color(0xFF8DA5B4),
    gradientEnd = Color(0xFFC4D3DF),
    circle1 = Color(0xFF9BB2C0), // Мягкий сизый
    circle2 = Color(0xFFADC0CC),
    circle3 = Color(0xFFB9CAD6),
    circle4 = Color(0xFFCAD8E1),
    circle5 = Color(0xFFD6E1E9),
    textColor = Color(0xFF2C3E50),
    textColorSecondary = Color(0xFF5D6D7E),
    iconTint = Color(0xFF5D6D7E),
    detailIconTint1 = Color(0xFF5499C7),
    detailIconTint2 = Color(0xFF7FB3D5),
    detailIconTint3 = Color(0xFFA9CCE3),
    sunIconTint = Color(0xFFD6E1E9),
    sunsetIconTint = Color(0xFFA9CCE3),
    cardColor = Color.White.copy(alpha = 0.25f),
    surfaceColor = Color(0xFF8DA5B4).copy(alpha = 0.2f),
    buttonColor = Color(0xFF5D6D7E),
    buttonTextColor = Color.White,
    buttonColorSecondary = Color(0xFFA9CCE3),
    buttonTextColorSecondary = Color(0xFF2C3E50),
    dividerColor = Color(0xFFD6E1E9).copy(alpha = 0.5f)
)

val drizzleNightColors = drizzleDayColors.copy(
    gradientStart = Color(0xFF192A56),
    gradientEnd = Color(0xFF273C75),
    textColor = Color.White,
    textColorSecondary = Color.White.copy(alpha = 0.7f),
    buttonTextColorSecondary = Color.White
)