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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyViewModel(
    private val getWeatherDay: GetWeatherUseCase,
    private val getAstronomy : GetAstronomyUseCase,
    private val getCoord : GetCurrentLocationUseCase,
    private val isGpsEnable : IsGpsEnabledUseCase,
    private val isPermission : IsPermissionUseCase,
    ): ViewModel() {



    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    var showGpsDialog by mutableStateOf(false)
    var requestPermissionTrigger by mutableStateOf(false) // Триггер для запуска pLauncher
    var locationState by mutableStateOf<Coord?>(null)
    var weatherFlow by mutableStateOf<Weather?>(null)



    fun getLocationFun() {
        viewModelScope.launch {
            _isLoading.value = true
           val result = getCoord()
            locationState = result
            getWeather("${result?.lat},${result?.lon}")
            }
        }

    fun getWeather(city: String){
        viewModelScope.launch {
            _isLoading.value = true
            getWeatherDay(city)
                .onSuccess {result -> weatherFlow = result }
                .onFailure {error ->  println(error.message.toString()) }
            _isLoading.value = false
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


