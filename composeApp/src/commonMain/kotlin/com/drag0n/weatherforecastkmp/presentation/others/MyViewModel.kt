package com.drag0n.weatherforecastkmp.presentation.others

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drag0n.weatherforecastkmp.domain.WeatherMapper
import com.drag0n.weatherforecastkmp.domain.model.WeatherState
import com.drag0n.weatherforecastkmp.domain.model.weatherType.WeatherColors
import com.drag0n.weatherforecastkmp.domain.useCases.GetCurrentLocationUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.GetWeatherUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.permission.IsGpsEnabledUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.permission.IsPermissionUseCase
import com.drag0n.weatherforecastkmp.data.room.Dao
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MyViewModel(
    private val getWeatherDay: GetWeatherUseCase,
    private val getCoord: GetCurrentLocationUseCase,
    private val isGpsEnable: IsGpsEnabledUseCase,
    private val isPermission: IsPermissionUseCase,
    private val room: Dao
) : ViewModel() {



    var statePermissionGps by mutableStateOf(false)
    var statePermissionLocation by mutableStateOf(false)

    init {
        statePermissionLocation = isPermissionFun("android.permission.ACCESS_FINE_LOCATION")
        statePermissionGps = isGpsEnableFun()
    }



    val stateLocation = MutableSharedFlow<String>(
        replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val stateColors = MutableStateFlow(WeatherColors.Default)


    @OptIn(ExperimentalCoroutinesApi::class)
    val weatherFlow = stateLocation.flatMapLatest { location ->
        flow {
            emit(WeatherState.Loading)
            getWeatherDay(location).onSuccess { result ->
                val weatherMapper = WeatherMapper.weatherData(result)
                val weatherWeek = WeatherMapper.weatherDataList(result.forecast.forecastday, result.location.localtime)
                val colors = getWeatherColors(weatherMapper.weatherType, weatherMapper.is_day)
                stateColors.value = colors
                emit(WeatherState.Success(weatherMapper,weatherWeek))
            }
                .onFailure { error ->
                    val isNetwork = when (error) {
                        is ConnectTimeoutException, is SocketTimeoutException, is HttpRequestTimeoutException -> true
                        is ResponseException -> false
                        else -> false
                    }

                    val msg =
                        if (isNetwork) "Проверьте ваше интернет соединение или включите VPN" else "Ошибка сервера или данных"

                    emit(
                        WeatherState.Error(
                            message = msg, isNetworkError = isNetwork
                        )
                    )

                }
        }
            .catch {
                println("Error:" + it.message.toString())
                emit(
                    WeatherState.Error(
                        message = "Произошла неизвестная ошибка", isNetworkError = false
                    )
                )
            }
    }
        .stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(5000), WeatherState.Loading
        )

    fun getLocationFun() {
        viewModelScope.launch {
            val result = getCoord()
            newLocation("${result?.lat},${result?.lon}")
        }
    }

    fun newLocation(value: String) = viewModelScope.launch {
        stateLocation.emit(value)
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