package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAlarmsUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var useCase: GetAlarmsUseCase

    private val alarm = Alarm(
        dbId = 1L, vibration = false, repeatDays = "", min = 0, hour = 8,
        disabled = false, soundActive = true, snoozeActive = false
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetAlarmsUseCase(repository)
    }

    @Test
    fun `invoke retorna flow de alarmes do repository`() = runTest {
        val list = listOf(alarm)
        every { repository.getAlarms() } returns flowOf(list)

        val result = useCase().first()

        assertEquals(list, result)
        verify { repository.getAlarms() }
    }

    @Test
    fun `invoke retorna flow vazio quando nao ha alarmes`() = runTest {
        every { repository.getAlarms() } returns flowOf(emptyList())

        val result = useCase().first()

        assertEquals(emptyList<Alarm>(), result)
    }
}