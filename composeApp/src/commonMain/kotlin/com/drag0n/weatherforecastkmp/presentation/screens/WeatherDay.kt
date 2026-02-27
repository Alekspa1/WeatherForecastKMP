package com.drag0n.weatherforecastkmp.presentation.screens


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Compress
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatDay
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherColors
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherType
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun WeatherScreen(
    weather: WeatherFormatDay,
    isLoading: Boolean = false, // Добавляем параметр
    onSearchClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {}
) {






    // Получаем цвета для фона в зависимости от погоды и времени суток
    val weatherColors = remember(weather.weatherType, weather.is_day) {
        getWeatherColors(weather.weatherType, weather.is_day)
    }

    // Анимация для фоновых кругов
    val infiniteTransition = rememberInfiniteTransition()

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

    // Анимация вращения для иконки обновления во время загрузки
    val infiniteTransitionForRefresh = rememberInfiniteTransition()
    val rotation by infiniteTransitionForRefresh.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing)
        )
    )

    // Обработчик нажатия на кнопку обновления
    val handleRefreshClick = {
        if (!isLoading) { // Используем переданное состояние
            onRefreshClick()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        weatherColors.gradientStart,
                        weatherColors.gradientEnd
                    )
                )
            )
    ) {
        // Фоновые анимированные круги
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Круг 1
            Box(
                modifier = Modifier
                    .size(400.dp)
                    .offset(
                        x = (200 + offsetX1).dp,
                        y = (250 + offsetY1).dp
                    )
                    .blur(70.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                weatherColors.circle1.copy(alpha = 0.45f),
                                weatherColors.circle1.copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            // Круг 2
            Box(
                modifier = Modifier
                    .size(350.dp)
                    .offset(
                        x = (450 + offsetX2).dp,
                        y = (550 + offsetY2).dp
                    )
                    .blur(80.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                weatherColors.circle2.copy(alpha = 0.4f),
                                weatherColors.circle2.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            // Круг 3
            Box(
                modifier = Modifier
                    .size((200 * scale2).dp)
                    .offset(
                        x = (650 + offsetX3).dp,
                        y = (200 + offsetY3).dp
                    )
                    .blur(60.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                weatherColors.circle3.copy(alpha = 0.4f),
                                weatherColors.circle3.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            // Круг 4
            Box(
                modifier = Modifier
                    .size(450.dp)
                    .offset(
                        x = (80 + offsetX4).dp,
                        y = (750 + offsetY4).dp
                    )
                    .blur(90.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                weatherColors.circle4.copy(alpha = 0.4f),
                                weatherColors.circle4.copy(alpha = 0.1f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )

            // Круг 5
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .offset(
                        x = 800.dp,
                        y = 100.dp
                    )
                    .blur(50.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                weatherColors.circle5.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        ),
                        CircleShape
                    )
            )
        }

        // Контент поверх фона
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Главная карточка погоды
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = weatherColors.cardColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = weather.city,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = weatherColors.textColor
                    )

                    Text(
                        text = weather.date,
                        fontSize = 24.sp,
                        color = weatherColors.textColorSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Иконка погоды в зависимости от типа
                    AsyncImage(
                        model = weather.icon,
                        contentDescription = weather.desc,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit,
                        // Можно добавить заглушки
                        placeholder = rememberVectorPainter(Icons.Default.Download),
                        error = rememberVectorPainter(Icons.Default.Error)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = weather.temp,
                        fontSize = 64.sp,
                        fontWeight = FontWeight.Light,
                        color = weatherColors.textColor
                    )

                    Text(
                        text = weather.feelslike,
                        fontSize = 18.sp,
                        color = weatherColors.textColorSecondary,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = weather.desc,
                        fontSize = 18.sp,
                        color = weatherColors.textColorSecondary
                    )
                }
            }

            //Spacer(modifier = Modifier.height(16.dp))

            // Верхняя панель с кнопками
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                //verticalAlignment = Alignment.CenterVertically
            ) {

                    // Кнопка поиска
                    Button(
                        onClick = onSearchClick,
                        modifier = Modifier.height(40.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = weatherColors.cardColor
                        ),
                        shape = RoundedCornerShape(20)
                    ) {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            tint = weatherColors.buttonTextColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Поиск",
                            color = weatherColors.buttonTextColor
                        )
                    }

                    //Spacer(modifier = Modifier.width(8.dp))

                    // Кнопка обновления с анимацией загрузки
                    Button(
                        onClick = handleRefreshClick,
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isLoading)
                                weatherColors.cardColor.copy(alpha = 0.5f)
                            else weatherColors.cardColor
                        ),
                        modifier = Modifier.height(40.dp),
                        shape = RoundedCornerShape(20)
                    ) {
                        if (isLoading) {
                            // Показываем индикатор загрузки
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = weatherColors.buttonTextColor
                            )
                        } else {
                            Icon(
                                Icons.Filled.Refresh,
                                contentDescription = null,
                                tint = weatherColors.buttonTextColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            if (isLoading) "Загрузка..." else "Обновить",
                            color = weatherColors.buttonTextColor
                        )
                    }

            }

            //Spacer(modifier = Modifier.height(16.dp))

            // Карточки с деталями

                // Ветер
                DetailCard(
                    modifier = Modifier.fillMaxSize(),
                    icon = Icons.Outlined.Air,
                    value = weather.wind,
                    label = "Ветер:",
                    iconTint = weatherColors.detailIconTint1,
                    colors = weatherColors
                )

                // Влажность
                DetailCard(
                    modifier = Modifier.fillMaxSize(),
                    icon = Icons.Outlined.WaterDrop,
                    value = weather.humidity,
                    label = "Влажность:",
                    iconTint = weatherColors.detailIconTint2,
                    colors = weatherColors
                )

                // Давление
                DetailCard(
                    modifier = Modifier.fillMaxSize(),
                    icon = Icons.Outlined.Compress,
                    value = weather.pressure,
                    label = "Давление:",
                    iconTint = weatherColors.detailIconTint3,
                    colors = weatherColors
                )


           // Spacer(modifier = Modifier.height(16.dp))

            // Карточка восхода и заката
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = weatherColors.cardColor
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    // Восход
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.WbSunny,
                            contentDescription = null,
                            tint = weatherColors.sunIconTint,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "Восход",
                                fontSize = 14.sp,
                                color = weatherColors.textColorSecondary
                            )
                            Text(
                                weather.sunrise,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = weatherColors.textColor
                            )
                        }
                    }

                    // Разделитель
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(40.dp)
                            .background(weatherColors.dividerColor)
                    )

                    // Закат
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.WbTwilight,
                            contentDescription = null,
                            tint = weatherColors.sunsetIconTint,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                "Закат",
                                fontSize = 14.sp,
                                color = weatherColors.textColorSecondary
                            )
                            Text(
                                weather.sunset,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = weatherColors.textColor
                            )
                        }
                    }
                }
            }
        }

        // Опционально: затемнение экрана во время загрузки
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .pointerInput(Unit) {} // Блокируем касания
            )
        }
    }
}

@Composable
private fun DetailCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    label: String,
    iconTint: Color,
    colors: WeatherColors
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.cardColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = label,
                fontSize = 15.sp,
                color = colors.textColorSecondary
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colors.textColor
            )


        }
    }
}









// Функция для получения цветов в зависимости от погоды и времени суток
fun getWeatherColors(weatherType: WeatherType, isDayTime: Boolean): WeatherColors {
    return when (weatherType) {
        WeatherType.SUNNY -> if (isDayTime) sunnyDayColors else sunnyNightColors
        WeatherType.CLOUDY -> if (isDayTime) cloudyDayColors else cloudyNightColors
        WeatherType.RAINY -> if (isDayTime) rainyDayColors else rainyNightColors
        WeatherType.SNOWY -> if (isDayTime) snowyDayColors else snowyNightColors
        WeatherType.STORMY -> if (isDayTime) stormyDayColors else stormyNightColors
        WeatherType.FOGGY -> if (isDayTime) foggyDayColors else foggyNightColors
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

@Preview
@Composable
fun Pre(){
    WeatherScreen(WeatherFormatDay("Москва","24 февраля", WeatherType.SUNNY,"Трололо","24°C","Ощущается как: 18°C","За окном: Переменная облачность с осадками","5 м/с","26%","750 мм/рт/ст","17:15","13:23",false))
}
