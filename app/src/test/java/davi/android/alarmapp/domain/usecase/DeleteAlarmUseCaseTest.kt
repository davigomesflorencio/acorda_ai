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

class DeleteAlarmUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var scheduler: AlarmScheduler
    private lateinit var useCase: DeleteAlarmUseCase

    private val alarm = Alarm(
        dbId = 1L, vibration = false, repeatDays = "", min = 0, hour = 8,
        disabled = false, soundActive = true, snoozeActive = false
    )

    @Before
    fun setUp() {
        repository = mockk()
        scheduler = mockk(relaxed = true)
        useCase = DeleteAlarmUseCase(repository, scheduler)
    }

    @Test
    fun `invoke deleta alarme e cancela agendamento`() = runTest {
        coJustRun { repository.delete(alarm) }

        useCase(alarm)

        coVerify { repository.delete(alarm) }
        coVerify { scheduler.cancel(alarm) }
    }
}