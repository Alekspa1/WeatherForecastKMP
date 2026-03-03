package com.drag0n.weatherforecastkmp.presentation.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.drag0n.weatherforecastkmp.domain.WeatherMapper
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherColors
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalFoundationApi::class)

@Composable

fun MainWeatherPager(
    onSearchClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {},
    openDrawerlick: () -> Unit = {},
    weather: Weather,
    weatherColors: WeatherColors

) {


    val titles = listOf("Сегодня", "3 дня", "Карта")


    // Аналог ViewPager2 (состояние контроллера)

    val pagerState = rememberPagerState(pageCount = { titles.size })
    val scope = rememberCoroutineScope()
    

    Column(modifier = Modifier.fillMaxSize()) {

            // Обертка для иконки и вкладок
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min), // Row будет высотой с TabRow
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { openDrawerlick() },
                    modifier = Modifier.fillMaxHeight() // Иконка растягивается на всю высоту вкладок
                ) {
                    Icon(Icons.Default.Menu, contentDescription = null)
                }

                PrimaryTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    modifier = Modifier.weight(1f),
                    containerColor = Color.Transparent, // Прозрачный фон
                    divider = {}, // Убираем стандартную линию снизу
                    // Настройка полоски (индикатора)
                    indicator = {
                        // В Material 3 (M3) позиции вкладок теперь берутся из context (this)
                        val modifier = Modifier.tabIndicatorOffset(pagerState.currentPage)

                        TabRowDefaults.PrimaryIndicator(
                            modifier = modifier,
                            width = 60.dp,        // Ширина полоски
                            height = 3.dp,        // Толщина
                            color = Color.White,  // БЕЛЫЙ ЦВЕТ
                            shape = RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp)
                        )
                    }
                ) {
                    titles.forEachIndexed { index, title ->
                        val selected = pagerState.currentPage == index

                        Tab(
                            selected = selected,
                            onClick = {
                                if (pagerState.currentPage != index) {
                                    scope.launch { pagerState.animateScrollToPage(index) }
                                }
                            },
                            // ЦВЕТА ТЕКСТА И ИКОНОК
                            selectedContentColor = Color.White,    // Активный — БЕЛЫЙ
                            unselectedContentColor = Color.Gray,   // Неактивный — СЕРЫЙ
                            text = {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleSmall,
                                    // Дополнительно можно менять жирность
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }

            HorizontalPager(

                state = pagerState,

                modifier = Modifier
                    .fillMaxSize()

            ) { pageIndex ->

                when (pageIndex) {

                    0 -> {
                        WeatherScreen(
                            onSearchClick = { onSearchClick() },
                            onRefreshClick = { onRefreshClick() },
                            weather = WeatherMapper.weatherData(weather),
                            weatherColors = weatherColors
                        )
                    }      // Твой первый экран

                    1 -> {
                        WeatherList(WeatherMapper.weatherDataList(weather.forecast.forecastday))
                    }

                    2 -> {
                        Box(Modifier.fillMaxSize()) {

                            CircularProgressIndicator(Modifier.align(Alignment.Center))

                        }
                    }

                }

            }

        }



}





