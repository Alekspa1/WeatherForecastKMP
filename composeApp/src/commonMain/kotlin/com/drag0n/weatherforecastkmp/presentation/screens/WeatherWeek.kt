package com.drag0n.weatherforecastkmp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.drag0n.weatherforecastkmp.domain.model.mapper.WeatherFormatWeek
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatHour


@Composable
fun WeatherList(weatherFormatWeek: List<WeatherFormatWeek>) {

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(

            modifier = Modifier.fillMaxSize(),

            // Тот самый отступ между элементами, о котором мы говорили

            verticalArrangement = Arrangement.spacedBy(12.dp),

            // Отступы для всего списка (чтобы не прилипало к краям при скролле)

            contentPadding = PaddingValues(8.dp)

        ) {
            weatherFormatWeek.forEach { format ->

                item(key = format.date) {
                    if (format.hours.isNotEmpty()) {
                        Text(text = format.date, color = Color.White)
                    }
                }
                items(
                    items = format.hours,
                    key = { hour -> "${format.date}_${hour.time}" }) { item ->

                    ItemWeather(item) // Вызываем твою Composable функцию

                }
            }
            // Отрисовка элементов

        }
    }


}


@Composable
fun ItemWeather(hour: WeatherFormatHour) {

    var isExpanded by remember { mutableStateOf(false) }

    Column(

        modifier = Modifier

            .fillMaxWidth()

        //.padding(8.dp) // Внешний отступ, чтобы карточки не липли к краям экрана

        //.animateContentSize()

    ) {

        // Основная карточка

        Card(

            modifier = Modifier

                .fillMaxWidth() // Занимаем всю ширину экрана

                .clip(RoundedCornerShape(16.dp)) // Сначала обрезаем углы

                .clickable { isExpanded = !isExpanded }, // Потом вешаем клик

            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0x80ADD8E6)
            ),

            elevation = CardDefaults.cardElevation(0.dp)

        ) {

            Column(modifier = Modifier.padding(12.dp)) {

                Text(

                    text = hour.desc,

                    modifier = Modifier.fillMaxWidth(),

                    textAlign = TextAlign.End,

                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White // Сделаем текст чуть аккуратнее

                )

                Row(

                    modifier = Modifier.fillMaxWidth(),

                    verticalAlignment = Alignment.CenterVertically

                ) {

                    Text(
                        hour.time, style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )


                    Spacer(modifier = Modifier.weight(1f))



                    Text(

                        text = hour.temp,

                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White

                    )

                }

            }

        }


        // Второстепенная карточка

        AnimatedVisibility(

            visible = isExpanded,

            enter = expandVertically() + fadeIn(),

            exit = shrinkVertically() + fadeOut()

        ) {

            Card(

                modifier = Modifier

                    .fillMaxWidth()

                    .padding(top = 8.dp),

                shape = RoundedCornerShape(16.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0x80ADD8E6)
                ),

                elevation = CardDefaults.cardElevation(0.dp)

            ) {

                Column(
                    modifier = Modifier.padding(16.dp),

                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(

                        text = hour.feelslike_c,
                        color = Color.White


                    )

                    Text(

                        text = hour.pressure,
                        color = Color.White


                    )

                    Text(

                        text = hour.humidity,
                        color = Color.White


                    )

                    Text(

                        text = hour.wind,
                        color = Color.White


                    )


                }


            }

        }

    }

}


@Preview(showBackground = true)
@Composable
fun Main() {
    WeatherList(listOf(WeatherFormatWeek(hours = listOf(WeatherFormatHour(), WeatherFormatHour()))))
}
