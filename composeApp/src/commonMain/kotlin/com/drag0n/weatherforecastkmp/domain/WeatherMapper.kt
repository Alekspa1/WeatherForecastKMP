package com.drag0n.weatherforecastkmp.domain


import com.drag0n.weatherforecastkmp.domain.model.mapper.WeatherFormatWeek
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Forecastday
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Hour
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatDay
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.WeatherFormatHour
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherType
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.math.roundToInt
import kotlin.time.Clock

object WeatherMapper {


    fun weatherData(weather: Weather): WeatherFormatDay {
        return WeatherFormatDay(
            city = weather.location.name,
            lat = weather.location.lat.toString() ,
            lon = weather.location.lon.toString(),
           // date = getCurrentFormattedDate(),
            date = formatToDateWithTime(weather.location.localtime),
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


    fun weatherDataList(forecast: List<Forecastday>, timeNow: String): List<WeatherFormatWeek> {

        return forecast.mapIndexed { dayIndex, forecastday ->
            val hours = (if (dayIndex == 0) {
                forecastday.hour
                    .filter { hour -> comparisonOfTime(hour.time, timeNow) }
                    .filterIndexed { hourIndex, _ -> hourIndex % 3 == 0 }
            } else {
                forecastday.hour.filterIndexed { hourIndex, _ -> hourIndex % 3 == 0 }
            })


            WeatherFormatWeek(
                formatToDate(forecastday.date),
                mapeperHour(hours)
            )
        }
    }

    fun comparisonOfTime(forecastTimeStr: String, cityLocalTimeStr: String): Boolean {
        // Парсим обе строки "как есть", игнорируя часовой пояс телефона
        val forecastDateTime = LocalDateTime.parse(forecastTimeStr.replace(" ", "T"))
        val currentCityDateTime = LocalDateTime.parse(cityLocalTimeStr.replace(" ", "T"))

        // Сравниваем объекты целиком (дата + время)
        // Это важно, чтобы в 23:00 вечера не отсеклись прогнозы на следующий день
        return forecastDateTime > currentCityDateTime
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
            1009 -> WeatherType.Overcast // пасмурно
            1087, 1273, 1276, 1282 -> WeatherType.STORMY
            // гроза
            1150, 1153, 1168, 1171 -> WeatherType.Drizzle
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
            humidity = "Влажность: ${hour.humidity} %",
            pressure = "Давление: ${(hour.pressure_mb * 0.75006).roundToInt()} мм/рт/ст",
            wind = "Скорость ветра: ${(hour.wind_kph / 3.6).roundToInt()} м/с",
            time = formatToTime(hour.time),
            temp = "${hour.temp_c.roundToInt()}°C",
        )
    }

    val russianMonths = listOf(
        "янв", "фев", "мар", "апр", "мая", "июня",
        "июля", "авг", "сен", "окт", "ноя", "дек"
    )

    private val MyDateFormatter = LocalDate.Format {
        day(Padding.NONE)
        char(' ')
        monthName(MonthNames(russianMonths)) // Создаем MonthNames один раз здесь
    }

    private val TimeOnlyFormatter = LocalDateTime.Format {
        hour(Padding.ZERO)   // Всегда две цифры (09, 14)
        char(':')
        minute(Padding.ZERO) // Всегда две цифры (05, 30)
    }

    fun formatToDateWithTime(timeString: String): String {
        // 1. Парсим строку "2024-03-07 11:00" напрямую
        // Это гарантирует, что дата останется такой, какой её прислал сервер
        val isoString = timeString.replace(" ", "T")
        val dateTime = LocalDateTime.parse(isoString)

        // 2. Используем форматтер (0.6.0+)
        val format = LocalDateTime.Format {
            day(Padding.NONE) // Будет "7", а не "07"
            char(' ')
            // Используем сокращенные названия месяцев
            monthName(MonthNames(russianMonths))
        }

        return dateTime.format(format)
    }
    fun formatToDate(timeString: String): String {
        val cleanDate = timeString.substringBefore(" ")
        return LocalDate.parse(cleanDate).format(MyDateFormatter)
    }

    fun formatToTime(timeString: String): String {
        // 1. Подготовка (WeatherAPI присылает "2024-03-07 15:00")
        val isoString = timeString.replace(" ", "T")

        // 2. Парсинг и применение готового шаблона в одну строку
        return LocalDateTime.parse(isoString).format(TimeOnlyFormatter)
    }
}


