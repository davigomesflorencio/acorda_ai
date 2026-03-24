package davi.android.alarmapp.domain.repository

import davi.android.alarmapp.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    fun getAlarms(): Flow<List<Alarm>>
    suspend fun getAlarmById(id: Long): Alarm?
    suspend fun insert(alarm: Alarm): Long
    suspend fun update(alarm: Alarm)
    suspend fun delete(alarm: Alarm)
}