package davi.android.alarmapp.di

import davi.android.alarmapp.data.repository.AlarmRepositoryImpl
import davi.android.alarmapp.domain.repository.AlarmRepository
import davi.android.alarmapp.domain.scheduler.AlarmScheduler
import davi.android.alarmapp.domain.vibration.ManagerVibrationAndSound
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { ManagerVibrationAndSound(androidContext()) }
    single<AlarmRepository> { AlarmRepositoryImpl(get()) }
    single { AlarmScheduler(androidContext()) }
}