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

class ToggleAlarmUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var scheduler: AlarmScheduler
    private lateinit var useCase: ToggleAlarmUseCase

    private val alarm = Alarm(
        dbId = 1L, vibration = false, repeatDays = "", min = 0, hour = 8,
        disabled = false, soundActive = true, snoozeActive = false
    )

    @Before
    fun setUp() {
        repository = mockk()
        scheduler = mockk(relaxed = true)
        useCase = ToggleAlarmUseCase(repository, scheduler)
    }

    @Test
    fun `invoke com active true atualiza disabled para true e agenda`() = runTest {
        val expectedAlarm = alarm.copy(disabled = true)
        coJustRun { repository.update(expectedAlarm) }

        useCase(alarm, active = true)

        coVerify { repository.update(expectedAlarm) }
        coVerify { scheduler.schedule(expectedAlarm) }
        coVerify(exactly = 0) { scheduler.cancel(any()) }
    }

    @Test
    fun `invoke com active false atualiza disabled para false e cancela`() = runTest {
        val expectedAlarm = alarm.copy(disabled = false)
        coJustRun { repository.update(expectedAlarm) }

        useCase(alarm, active = false)

        coVerify { repository.update(expectedAlarm) }
        coVerify { scheduler.cancel(expectedAlarm) }
        coVerify(exactly = 0) { scheduler.schedule(any()) }
    }
}