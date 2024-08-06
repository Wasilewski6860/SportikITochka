package com.example.sportikitochka.app

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.example.sportikitochka.R
import com.example.sportikitochka.di.dataModule
import com.example.sportikitochka.di.domainModule
import com.example.sportikitochka.di.presentationModule
import io.appmetrica.analytics.AppMetrica
import io.appmetrica.analytics.AppMetricaConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class App: Application() {

    override fun onCreate() {
        super.onCreate()
        val toString = resources.getString(R.string.yandex_maps_key);
        println(toString)
        world.mappable.mapkit.MapKitFactory.setApiKey("pk_tOwnkqqtgDlBNxnYBtLnIrSheJSuRfsVDbkCjugMHeZfUoIuhKkLUrqdtKWNaSPb")
        val config = AppMetricaConfig.newConfigBuilder("0ee9624d-3997-4356-94d4-052ff53bb44d").withRevenueAutoTrackingEnabled(true).build()
        AppMetrica.activate(this, config)
        AppMetrica.enableActivityAutoTracking(this)

        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(dataModule, domainModule, presentationModule))
        }
    }

}