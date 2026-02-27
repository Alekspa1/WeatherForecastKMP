package com.drag0n.weatherforecastkmp.presentation.others

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drag0n.weatherforecastkmp.domain.model.Coord
import com.drag0n.weatherforecastkmp.domain.model.WeatherState
import com.drag0n.weatherforecastkmp.domain.useCases.GetCurrentLocationUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.GetWeatherUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.permission.IsGpsEnabledUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.permission.IsPermissionUseCase
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyViewModel(
    private val getWeatherDay: GetWeatherUseCase,
    private val getCoord: GetCurrentLocationUseCase,
    private val isGpsEnable: IsGpsEnabledUseCase,
    private val isPermission: IsPermissionUseCase,
) : ViewModel() {



    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    var statePermissionGps by mutableStateOf(false)
    var statePermissionLocation by mutableStateOf(false)

    init {
        statePermissionLocation = isPermissionFun("android.permission.ACCESS_FINE_LOCATION")
        statePermissionGps = isGpsEnableFun()
    }


    var locationState by mutableStateOf<Coord?>(null)
    var weatherFlow by mutableStateOf<WeatherState>(WeatherState.Loading)


    fun getLocationFun() {
        viewModelScope.launch {
            _isLoading.value = true
            weatherFlow = WeatherState.Loading
            val result = getCoord()
            locationState = result
            getWeather("${result?.lat},${result?.lon}")
        }
    }

    fun getWeather(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            weatherFlow = WeatherState.Loading
            getWeatherDay(city)
                .onSuccess { result -> weatherFlow = WeatherState.Success(result) }
                .onFailure { error ->
                    val isNetwork = when (error) {
                        is ConnectTimeoutException,
                        is SocketTimeoutException,
                        is HttpRequestTimeoutException -> true

                        is ResponseException -> false

                        else -> false
                    }

                    val msg = if (isNetwork) "Проверьте ваше интернет соединение или включите VPN" else "Ошибка сервера или данных"

                    weatherFlow = WeatherState.Error(
                        message = msg,
                        isNetworkError = isNetwork
                    )
                }
            _isLoading.value = false
        }
    }





    fun isPermissionFun(permission: String): Boolean {
        statePermissionLocation = isPermission(permission)
        return isPermission(permission)
    }

    fun isGpsEnableFun(): Boolean {
        statePermissionGps = isGpsEnable()
        return isGpsEnable()
    }

}