package com.drag0n.weatherforecastkmp

import android.app.Application
import com.drag0n.weatherforecastkmp.data.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidContext(this@MyApp)
        }
    }
}