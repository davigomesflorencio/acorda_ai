package davi.android.alarmapp.di

import davi.android.alarmapp.domain.vibration.ManagerVibrationAndSound
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val repositoryModule = module {
    single { ManagerVibrationAndSound(androidContext()) }
}
