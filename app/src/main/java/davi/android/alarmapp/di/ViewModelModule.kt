package davi.android.alarmapp.di

import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import davi.android.alarmapp.presentation.viewmodel.AlarmsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AddAlarmViewModel(androidApplication(), get()) }
    viewModel { AlarmsViewModel(androidApplication(), get()) }
}