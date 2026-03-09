package davi.android.alarmapp.di

import davi.android.alarmapp.worker.worker.AlarmWorker
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workerModule = module {
    worker { AlarmWorker(androidContext(), get()) }
}
