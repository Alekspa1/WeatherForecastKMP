package com.drag0n.weatherforecastkmp.domain

import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatDay
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherType
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Clock

object WeatherMapper {


    fun WeatherData(weather: Weather): WeatherFormatDay {
        return WeatherFormatDay(
            city = weather.location.name,
            date = getCurrentFormattedDate(),
            weatherType = typewWeather(weather.current.condition.code),
            icon = "https:${weather.current.condition.icon}",
            temp = "${weather.current.temp_c.roundToInt()}°C",
            feelslike = "Ощущается как: ${weather.current.feelslike_c.roundToInt()} °C",
            desc = "За окном: ${weather.current.condition.text}",
            wind = "${(weather.current.wind_kph / 3.6).roundToInt()} м/с",
            humidity = "${weather.current.humidity} %",
            pressure = "${(weather.current.pressure_mb * 0.75006).roundToInt()} мм/рт/ст",
            sunrise = formatAstroTime(weather.forecast.forecastday[0].astro.sunrise) ,
            sunset = formatAstroTime(weather.forecast.forecastday[0].astro.sunset),
            is_day = weather.current.is_day == 1


        )
    }

    private fun getCurrentFormattedDate(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val day = now.day

        val monthName = when (now.month) {
            Month.JANUARY -> "января"
            Month.FEBRUARY -> "феваля"
            Month.MARCH -> "марта"
            Month.APRIL -> "апреля"
            Month.MAY -> "мая"
            Month.JUNE -> "июня"
            Month.JULY -> "июля"
            Month.AUGUST -> "августа"
            Month.SEPTEMBER -> "сентября"
            Month.OCTOBER -> "октября"
            Month.NOVEMBER -> "ноября"
            Month.DECEMBER -> "декабря"
        }

        return "$day $monthName"
    }

    private fun formatAstroTime(time: String): String {
        val parts = time.split(" ", ":")
        if (parts.size < 3) return time

        var hour = parts[0].toInt()
        val minute = parts[1]
        val amPm = parts[2].uppercase()

        if (amPm == "PM" && hour < 12) hour += 12
        if (amPm == "AM" && hour == 12) hour = 0

        val formattedHour = hour.toString().padStart(2, '0')
        return "$formattedHour:$minute"
    }

    private fun typewWeather(code: Int): WeatherType {
        return when (code) {
            //1009 -> insertBackground(R.drawable.img_pasm) // пасмурно
            1087, 1273, 1276, 1282 -> WeatherType.STORMY
            // гроза
            //1150, 1153, 1168, 1171 -> insertBackground(R.drawable.img_2_day) // морось
            1063, 1072, 1180, 1183, 1186, 1189, 1192, 1195, 1198, 1201, 1240, 1243, 1246, 1249, 1252
                -> WeatherType.RAINY
            // дождь
            1066, 1069, 1114, 1117, 1204, 1207, 1210, 1213, 1216, 1219, 1222, 1225, 1237, 1255, 1258, 1261, 1264
                -> WeatherType.SNOWY
            // снег
            1030, 1147, 1135 -> WeatherType.FOGGY // туман
            1000 -> WeatherType.SUNNY // Чистое небо
            else -> WeatherType.CLOUDY
        }
    }
}