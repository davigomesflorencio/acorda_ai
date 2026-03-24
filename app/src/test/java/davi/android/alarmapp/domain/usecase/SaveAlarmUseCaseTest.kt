package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import davi.android.alarmapp.domain.scheduler.AlarmScheduler
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SaveAlarmUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var scheduler: AlarmScheduler
    private lateinit var useCase: SaveAlarmUseCase

    private val alarm = Alarm(
        dbId = 0L, vibration = false, repeatDays = "", min = 30, hour = 7,
        disabled = false, soundActive = true, snoozeActive = false, name = "Novo"
    )

    @Before
    fun setUp() {
        repository = mockk()
        scheduler = mockk(relaxed = true)
        useCase = SaveAlarmUseCase(repository, scheduler)
    }

    @Test
    fun `invoke insere alarme e agenda com id gerado`() = runTest {
        val generatedId = 5L
        coEvery { repository.insert(alarm) } returns generatedId

        val result = useCase(alarm)

        assertEquals(generatedId, result)
        coVerify { repository.insert(alarm) }
        coVerify { scheduler.schedule(alarm.copy(dbId = generatedId)) }
    }

    @Test
    fun `invoke retorna id gerado pelo repository`() = runTest {
        coEvery { repository.insert(alarm) } returns 99L

        val result = useCase(alarm)

        assertEquals(99L, result)
    }
}