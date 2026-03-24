package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository

class GetAlarmByIdUseCase(private val repository: AlarmRepository) {
    suspend operator fun invoke(id: Long): Alarm? = repository.getAlarmById(id)
}