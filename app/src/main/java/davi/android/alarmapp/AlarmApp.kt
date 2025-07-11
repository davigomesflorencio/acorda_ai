package davi.android.alarmapp

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.microsoft.clarity.Clarity
import com.microsoft.clarity.ClarityConfig
import com.microsoft.clarity.models.LogLevel
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

        val config = ClarityConfig(
            projectId = "s9bed1ha0d",
            logLevel = LogLevel.Debug // Note: Use "LogLevel.Verbose" value while testing to debug initialization issues.
        )
        Clarity.initialize(applicationContext, config)

        val myConfig = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()

        WorkManager.initialize(this, myConfig)
    }

    override fun onTerminate() {
        super.onTerminate()
        stopKoin()
    }
}