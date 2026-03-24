package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import davi.android.alarmapp.domain.scheduler.AlarmScheduler

class DeleteAlarmUseCase(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler,
) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.delete(alarm)
        scheduler.cancel(alarm)
    }
}