package davi.android.alarmapp.domain.usecase

import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class GetAlarmByIdUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var useCase: GetAlarmByIdUseCase

    private val alarm = Alarm(
        dbId = 1L, vibration = false, repeatDays = "", min = 0, hour = 8,
        disabled = false, soundActive = true, snoozeActive = false
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetAlarmByIdUseCase(repository)
    }

    @Test
    fun `invoke retorna alarme quando encontrado`() = runTest {
        coEvery { repository.getAlarmById(1L) } returns alarm

        val result = useCase(1L)

        assertEquals(alarm, result)
        coVerify { repository.getAlarmById(1L) }
    }

    @Test
    fun `invoke retorna null quando alarme nao encontrado`() = runTest {
        coEvery { repository.getAlarmById(99L) } returns null

        val result = useCase(99L)

        assertNull(result)
        coVerify { repository.getAlarmById(99L) }
    }
}