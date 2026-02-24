package com.drag0n.weatherforecastkmp.data.di



import com.drag0n.weatherforecastkmp.data.repository.GetcoordInIpImpl
import com.drag0n.weatherforecastkmp.data.repository.WeatherImp
import com.drag0n.weatherforecastkmp.domain.repository.LocationRepository
import com.drag0n.weatherforecastkmp.domain.repository.WeatherRepository
import com.drag0n.weatherforecastkmp.domain.useCases.GetAstronomyUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.GetCurrentLocationUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.GetWeatherUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.permission.IsGpsEnabledUseCase
import com.drag0n.weatherforecastkmp.domain.useCases.permission.IsPermissionUseCase
import com.drag0n.weatherforecastkmp.presentation.MyViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val moduleLocation: Module



val appModule = module {

    single<HttpClient> {

        HttpClient {
            install(Logging) {

                logger = object : Logger {
                    override fun log(message: String) {
                    println(message)
                    }
                }
                level = LogLevel.BODY
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 15000 // 15 секунд на весь запрос
                connectTimeoutMillis = 15000 // 15 секунд на установку соединения
                socketTimeoutMillis = 15000  // 15 секунд на обмен данными
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single<WeatherRepository> { WeatherImp(get()) }
    single<LocationRepository>(named("IP_LOCATION")) { GetcoordInIpImpl(get()) }


    factory<GetWeatherUseCase> { GetWeatherUseCase(get()) }
    factory<GetAstronomyUseCase> { GetAstronomyUseCase(get()) }
    factory<GetCurrentLocationUseCase> { GetCurrentLocationUseCase(get()) }
    factory<IsGpsEnabledUseCase> { IsGpsEnabledUseCase(get()) }
    factory<IsPermissionUseCase> { IsPermissionUseCase(get()) }


    viewModelOf(::MyViewModel)

}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule, moduleLocation)
    }
}

