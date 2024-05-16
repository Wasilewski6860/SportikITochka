package com.example.sportikitochka.app

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.example.sportikitochka.R
import com.example.sportikitochka.di.appModule
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

//        com.yandex.mapkit.MapKitFactory.setApiKey(toString)
//        com.yandex.mapkit.MapKitFactory.initialize(this)
////        0ee9624d-3997-4356-94d4-052ff53bb44d
//        val config = AppMetricaConfig.newConfigBuilder("0ee9624d-3997-4356-94d4-052ff53bb44d").build()
//        // Initializing the AppMetrica SDK.
//        AppMetrica.activate(this, config)
//        AppMetrica.enableActivityAutoTracking(this)

//        VK
//        MapGlobalConfig.setMapGlobalConfig(
//            MapViewConfig(
//                apiKey = "bcfbfa15bcfbfa15bcfbfa1574bfe3d033bbcfbbcfbfa15dac2eacf5a196a66f29a03e3"
//            )
//        )

        // Reading API key from BuildConfig.
        // Do not forget to add your MAPKIT_API_KEY property to local.properties file.
//        MapKitFactory.setApiKey("pk_tOwnkqqtgDlBNxnYBtLnIrSheJSuRfsVDbkCjugMHeZfUoIuhKkLUrqdtKWNaSPb")

        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(appModule, dataModule, domainModule, presentationModule))
        }

        // Ключ вк карты bcfbfa15bcfbfa15bcfbfa1574bfe3d033bbcfbbcfbfa15dac2eacf5a196a66f29a03e3
        // Ключ mappable карты pk_tOwnkqqtgDlBNxnYBtLnIrSheJSuRfsVDbkCjugMHeZfUoIuhKkLUrqdtKWNaSPb


//        val config = YandexMetricaConfig.newConfigBuilder("0ee9624d-3997-4356-94d4-052ff53bb44d").withLogs().build()
////        YandexMetrica.activate(this, config)
//
//        // Initialize AppMetrica in the main thread
//        // Initialize AppMetrica in the main thread
//        val mainHandler = Handler(Looper.getMainLooper())
//        mainHandler.post(Runnable { YandexMetrica.activate(applicationContext, config) })
    }

}