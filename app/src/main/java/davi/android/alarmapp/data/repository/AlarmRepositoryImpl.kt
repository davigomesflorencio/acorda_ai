package davi.android.alarmapp.data.repository

import davi.android.alarmapp.data.dao.AlarmDao
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class AlarmRepositoryImpl(private val alarmDao: AlarmDao) : AlarmRepository {
    override fun getAlarms(): Flow<List<Alarm>> = alarmDao.getAlarms()
    override suspend fun getAlarmById(id: Long): Alarm? = alarmDao.getAlarm(id)
    override suspend fun insert(alarm: Alarm): Long = alarmDao.insert(alarm)
    override suspend fun update(alarm: Alarm) = alarmDao.update(alarm)
    override suspend fun delete(alarm: Alarm) = alarmDao.delete(alarm)
}