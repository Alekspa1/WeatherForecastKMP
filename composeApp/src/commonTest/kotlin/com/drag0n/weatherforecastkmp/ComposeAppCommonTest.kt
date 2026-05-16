package com.drag0n.weatherforecastkmp

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

// 1. Создаем фальшивое API для теста, чтобы не ходить в реальный интернет
class MockWeatherApi : YourApiInterfaceName { // Замените на имя вашего интерфейса API
    var shouldReturnSuccess = true
    
    override suspend fun getCurrent(api: String, latLon: String, lang: String): Response<NewCurrent> {
        return if (shouldReturnSuccess) {
            // Возвращаем успешный пустой объект вашей модели погоды
            Response.success(NewCurrent(/* заполните базовыми тестовыми данными */))
        } else {
            // Имитируем ошибку сервера
            Response.error(400, ResponseBody.create(null, "Error"))
        }
    }
}

class WeatherViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testViewModel_SuccessState_OnLocationReceived() = runTest {
        // 2. Предустановка (Given)
        val mockApi = MockWeatherApi()
        // Передаем фальшивое API в вашу реальную ViewModel
        val viewModel = YourViewModelName(mockApi) 

        // 3. Действие (When) - Имитируем, что iOS-код определил локацию и передал её
        // Вызовите метод вашей ViewModel, который запускает stateLocation.emit(...)
        viewModel.setLocation("55.75,37.62") 

        // 4. Проверка (Then)
        // Проверяем, что состояние ViewModel переключилось в Success
        val currentState = viewModel.stateCurrent.value
        assertTrue(currentState is StateCurrent.Success, "Экран должен показать погоду!")
    }
}
