package com.drag0n.weatherforecastkmp.domain


import com.drag0n.weatherforecastkmp.domain.model.mapper.ForecastDateFormat
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Forecastday
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Hour
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatDay
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatHour
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherType
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.time.Instant

object WeatherMapper {


    fun weatherData(weather: Weather): WeatherFormatDay {
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
            sunrise = formatAstroTime(weather.forecast.forecastday[0].astro.sunrise),
            sunset = formatAstroTime(weather.forecast.forecastday[0].astro.sunset),
            is_day = weather.current.is_day == 1


        )
    }


    fun weatherDataList(forecast: List<Forecastday>): List<ForecastDateFormat> {

        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

        return forecast.mapIndexed { dayIndex, forecastday ->
            val hours = (if (dayIndex == 0) {
                forecastday.hour
                    .filter { hour -> comparisonOfTime(hour.time_epoch, now) }
                    .filterIndexed { hourIndex, _ -> hourIndex % 3 == 0 }
            } else {
                forecastday.hour.filterIndexed { hourIndex, _ -> hourIndex % 3 == 0 }
            })


            ForecastDateFormat(
                formatToDate(forecastday.hour[dayIndex].time_epoch),
                mapeperHour(hours)
            )
        }
    }

    private fun comparisonOfTime(timeEpoch: String, currentTime: LocalTime): Boolean {
        // 1. Превращаем секунды в объект даты/времени
        val instant = Instant.fromEpochSeconds(timeEpoch.toLong())

        // 2. Получаем локальное время (LocalTime) для текущего часового пояса
        val hourTime = instant.toLocalDateTime(TimeZone.currentSystemDefault()).time

        // 3. Сравниваем (например, 15:00 > 11:30)
        return hourTime > currentTime
    }

    private fun getCurrentFormattedDate(): String {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val day = now.day

        val monthName = when (now.month) {
            Month.JANUARY -> "января"
            Month.FEBRUARY -> "февраля"
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

    private fun mapeperHour(hourList: List<Hour>) = hourList.map { hour ->
        WeatherFormatHour(
            desc = hour.condition.text,
            feelslike_c = "Ощущается как: ${hour.feelslike_c.roundToInt()}°C",
            humidity = "${hour.humidity} %",
            pressure = "${(hour.pressure_mb * 0.75006).roundToInt()} мм/рт/ст",
            wind = "${(hour.wind_mph / 3.6).roundToInt()} м/с",
            time = formatToTime(hour.time_epoch),
            temp = "${hour.temp_c.roundToInt()}°C",
        )
    }

    private val russianMonths = MonthNames(
        listOf("янв", "фев", "мар", "апр", "мая", "июн", "июл", "авг", "сен", "окт", "ноя", "дек")
    )

    fun formatToDate(epochSeconds: String): String {
        val instant = Instant.fromEpochSeconds(epochSeconds.toLong())
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val format = LocalDateTime.Format {
            this@Format.day(padding = Padding.NONE)
            // Убирает ведущий ноль (будет "1", а не "01")
            char(' ')
            monthName(russianMonths)
        }

        return dateTime.format(format)
    }

    fun formatToTime(epochSeconds: String): String {
        val instant = Instant.fromEpochSeconds(epochSeconds.toLong())
        val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val format = LocalDateTime.Format {
            hour()   // "14"
            char(':')
            minute() // "00"
        }

        return dateTime.format(format)
    }
}


