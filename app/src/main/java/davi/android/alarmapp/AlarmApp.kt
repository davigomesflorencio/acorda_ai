package davi.android.alarmapp

import android.app.Application
import davi.android.alarmapp.di.injectMobileFeature
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin

class AlarmApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AlarmApp)
            injectMobileFeature()
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}