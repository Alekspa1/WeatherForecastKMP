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
import androidx.compose.ui.text.style.TextAlign
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
    weather: WeatherFormatDay, // Добавляем параметр
    onSearchClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    weatherColors: WeatherColors = WeatherColors.Default
) {
    // Обработчик нажатия на кнопку обновления

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Контент поверх фона
        Column(
            modifier = Modifier
                .fillMaxSize()
               // .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = weather.desc,
                        fontSize = 18.sp,
                        color = weatherColors.textColorSecondary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }


            // Верхняя панель с кнопками
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
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


                // Кнопка обновления с анимацией загрузки
                Button(
                    onClick = { onRefreshClick() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = weatherColors.cardColor
                    ),
                    modifier = Modifier.height(40.dp),
                    shape = RoundedCornerShape(20)
                ) {

                    Icon(
                        Icons.Filled.Refresh,
                        contentDescription = null,
                        tint = weatherColors.buttonTextColor,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Обновить",
                        color = weatherColors.buttonTextColor
                    )
                }

            }


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


@Preview
@Composable
fun Pre() {
    WeatherScreen(
       weather =  WeatherFormatDay(
            "Москва",
           "",
           "",
            "24 февраля",
            WeatherType.SUNNY,
            "Трололо",
            "24°C",
            "Ощущается как: 18°C",
            "За окном: Переменная облачность с осадками",
            "5 м/с",
            "26%",
            "750 мм/рт/ст",
            "17:15",
            "13:23",
            false,
        ),
        weatherColors = WeatherColors.Default
    )
}
