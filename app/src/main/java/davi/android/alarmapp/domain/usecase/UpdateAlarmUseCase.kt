package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import davi.android.alarmapp.domain.scheduler.AlarmScheduler

class UpdateAlarmUseCase(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler,
) {
    suspend operator fun invoke(alarm: Alarm) {
        repository.update(alarm)
        if (alarm.disabled) {
            scheduler.schedule(alarm)
        } else {
            scheduler.cancel(alarm)
        }
    }
}