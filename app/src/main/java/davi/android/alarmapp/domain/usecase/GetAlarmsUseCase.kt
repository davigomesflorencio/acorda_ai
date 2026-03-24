package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class GetAlarmsUseCase(private val repository: AlarmRepository) {
    operator fun invoke(): Flow<List<Alarm>> = repository.getAlarms()
}