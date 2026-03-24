package davi.android.alarmapp.data.repository

import davi.android.alarmapp.data.dao.AlarmDao
import davi.android.alarmapp.domain.model.Alarm
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AlarmRepositoryImplTest {

    private lateinit var alarmDao: AlarmDao
    private lateinit var repository: AlarmRepositoryImpl

    private val testAlarm = Alarm(
        dbId = 1L,
        vibration = true,
        repeatDays = "1,2,3",
        min = 30,
        hour = 7,
        disabled = false,
        soundActive = true,
        snoozeActive = false,
        name = "Test Alarm",
        ringtone = ""
    )

    @Before
    fun setUp() {
        alarmDao = mockk()
        repository = AlarmRepositoryImpl(alarmDao)
    }

    @Test
    fun `getAlarms retorna flow do dao`() = runTest {
        val alarmList = listOf(testAlarm)
        every { alarmDao.getAlarms() } returns flowOf(alarmList)

        val result = repository.getAlarms().first()

        assertEquals(alarmList, result)
        verify { alarmDao.getAlarms() }
    }

    @Test
    fun `getAlarmById retorna alarme quando encontrado`() = runTest {
        coEvery { alarmDao.getAlarm(1L) } returns testAlarm

        val result = repository.getAlarmById(1L)

        assertEquals(testAlarm, result)
        coVerify { alarmDao.getAlarm(1L) }
    }

    @Test
    fun `getAlarmById retorna null quando nao encontrado`() = runTest {
        coEvery { alarmDao.getAlarm(99L) } returns null

        val result = repository.getAlarmById(99L)

        assertNull(result)
        coVerify { alarmDao.getAlarm(99L) }
    }

    @Test
    fun `insert chama dao e retorna id gerado`() = runTest {
        val generatedId = 42L
        coEvery { alarmDao.insert(testAlarm) } returns generatedId

        val result = repository.insert(testAlarm)

        assertEquals(generatedId, result)
        coVerify { alarmDao.insert(testAlarm) }
    }

    @Test
    fun `update chama dao update com o alarme`() = runTest {
        coEvery { alarmDao.update(testAlarm) } returns Unit

        repository.update(testAlarm)

        coVerify { alarmDao.update(testAlarm) }
    }

    @Test
    fun `delete chama dao delete com o alarme`() = runTest {
        coEvery { alarmDao.delete(testAlarm) } returns Unit

        repository.delete(testAlarm)

        coVerify { alarmDao.delete(testAlarm) }
    }
}