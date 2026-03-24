package davi.android.alarmapp.di

import davi.android.alarmapp.domain.usecase.DeleteAlarmUseCase
import davi.android.alarmapp.domain.usecase.GetAlarmByIdUseCase
import davi.android.alarmapp.domain.usecase.GetAlarmsUseCase
import davi.android.alarmapp.domain.usecase.SaveAlarmUseCase
import davi.android.alarmapp.domain.usecase.ToggleAlarmUseCase
import davi.android.alarmapp.domain.usecase.UpdateAlarmUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetAlarmsUseCase(get()) }
    factory { GetAlarmByIdUseCase(get()) }
    factory { SaveAlarmUseCase(get(), get()) }
    factory { UpdateAlarmUseCase(get(), get()) }
    factory { DeleteAlarmUseCase(get(), get()) }
    factory { ToggleAlarmUseCase(get(), get()) }
}