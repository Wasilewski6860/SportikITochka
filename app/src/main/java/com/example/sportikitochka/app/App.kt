package com.example.sportikitochka.app

import android.app.Application
import android.widget.Toast
import com.example.sportikitochka.di.appModule
import com.example.sportikitochka.di.dataModule
import com.example.sportikitochka.di.domainModule
import com.example.sportikitochka.di.presentationModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(appModule, dataModule, domainModule, presentationModule))
        }
    }

}