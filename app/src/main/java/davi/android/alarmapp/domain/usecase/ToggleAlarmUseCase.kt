package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import davi.android.alarmapp.domain.scheduler.AlarmScheduler

class ToggleAlarmUseCase(
    private val repository: AlarmRepository,
    private val scheduler: AlarmScheduler,
) {
    // disabled=true significa alarme ATIVO no modelo de dados deste projeto
    suspend operator fun invoke(alarm: Alarm, active: Boolean) {
        val updated = alarm.copy(disabled = active)
        repository.update(updated)
        if (active) {
            scheduler.schedule(updated)
        } else {
            scheduler.cancel(updated)
        }
    }
}