package com.drag0n.weatherforecastkmp.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)

@Composable

fun MainWeatherPager(
    isLoading: Boolean = false, // Добавляем параметр
    onSearchClick: () -> Unit = {},
    onRefreshClick: () -> Unit = {}
) {


    val titles = listOf("Сегодня", "3 дня", "Карта")


    // Аналог ViewPager2 (состояние контроллера)

    val pagerState = rememberPagerState(pageCount = { titles.size })

    val scope = rememberCoroutineScope()



    Column(modifier = Modifier.fillMaxSize()) {

        // 1. Аналог TabLayout

        PrimaryTabRow(selectedTabIndex = pagerState.currentPage) {

            titles.forEachIndexed { index, title ->

                Tab(

                    selected = pagerState.currentPage == index,

                    onClick = {

                        if (pagerState.currentPage != index) {
                            scope.launch { pagerState.animateScrollToPage(index) }
                        }

                    },

                    text = { Text(title) }

                )

            }

        }


        // 2. Аналог ViewPager2

        HorizontalPager(

            state = pagerState,

            modifier = Modifier.fillMaxSize()

        ) { pageIndex ->

            when (pageIndex) {

                0 -> {
                    WeatherScreen(
                        isLoading = isLoading,
                        onSearchClick = { onSearchClick() },
                        onRefreshClick = { onRefreshClick() })
                }      // Твой первый экран

                1 -> {}

                2 -> {
                    Box(Modifier.fillMaxSize()) {

                        CircularProgressIndicator(Modifier.align(Alignment.Center))

                    }
                }

            }

        }

    }

}