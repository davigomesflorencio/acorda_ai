package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import davi.android.alarmapp.domain.scheduler.AlarmScheduler
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateAlarmUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var scheduler: AlarmScheduler
    private lateinit var useCase: UpdateAlarmUseCase

    private val baseAlarm = Alarm(
        dbId = 1L, vibration = false, repeatDays = "", min = 0, hour = 8,
        disabled = false, soundActive = true, snoozeActive = false
    )

    @Before
    fun setUp() {
        repository = mockk()
        scheduler = mockk(relaxed = true)
        useCase = UpdateAlarmUseCase(repository, scheduler)
    }

    @Test
    fun `invoke atualiza e agenda quando disabled e true (alarme ativo)`() = runTest {
        val activeAlarm = baseAlarm.copy(disabled = true)
        coJustRun { repository.update(activeAlarm) }

        useCase(activeAlarm)

        coVerify { repository.update(activeAlarm) }
        coVerify { scheduler.schedule(activeAlarm) }
        coVerify(exactly = 0) { scheduler.cancel(any()) }
    }

    @Test
    fun `invoke atualiza e cancela quando disabled e false (alarme desativado)`() = runTest {
        val inactiveAlarm = baseAlarm.copy(disabled = false)
        coJustRun { repository.update(inactiveAlarm) }

        useCase(inactiveAlarm)

        coVerify { repository.update(inactiveAlarm) }
        coVerify { scheduler.cancel(inactiveAlarm) }
        coVerify(exactly = 0) { scheduler.schedule(any()) }
    }
}