package com.drag0n.weatherforecastkmp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.domain.model.weatherForecast.Weather

import com.drag0n.weatherforecastkmp.domain.useCases.GetAstronomyUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.GetCurrentLocationUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.GetWeatherUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.permission.IsGpsEnabledUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.permission.IsPermissionUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyViewModel(
    private val getWeatherDay: GetWeatherUseCase,
    private val getAstronomy : GetAstronomyUseCase,
    private val getCoord : GetCurrentLocationUseCase,
    private val isGpsEnable : IsGpsEnabledUseCase,
    private val isPermission : IsPermissionUseCase,
    ): ViewModel() {




    var showGpsDialog by mutableStateOf(false)
    var requestPermissionTrigger by mutableStateOf(false) // Триггер для запуска pLauncher
    var locationState by mutableStateOf<Coord?>(null)
    var weatherFlow by mutableStateOf<Weather?>(null)



    fun getLocationFun() {
        viewModelScope.launch {
            delay(1000)
           val result = getCoord()
            locationState = result
            val weather = getWeather("${result?.lat},${result?.lon}")
            println(weather.toString())

            }
        }

    fun getWeather(city: String){
        viewModelScope.launch {
            getWeatherDay(city)
                .onSuccess {result -> weatherFlow = result }
                .onFailure {error ->  println(error.message.toString()) }
        }
    }


    fun isPermissionFun(permission: String ) : Boolean{
        requestPermissionTrigger = !isPermission(permission)
        return isPermission(permission)
    }

    fun isGpsEnableFun(){
        showGpsDialog = !isGpsEnable()
    }

    fun resetTriggers() {
        requestPermissionTrigger = false
    }
}


