package br.dev.saed.voleibr

import android.app.Application
import br.dev.saed.voleibr.model.di.appModule
import br.dev.saed.voleibr.model.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MercadoApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()

            androidContext(this@MercadoApplication)

            modules(appModule, storageModule)
        }

    }

}