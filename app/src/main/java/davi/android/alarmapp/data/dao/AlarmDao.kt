package davi.android.alarmapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import davi.android.alarmapp.domain.model.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: Alarm): Long

    @Delete
    suspend fun delete(alarm: Alarm)

    @Update
    suspend fun update(alarm: Alarm)

    @Query("SELECT * FROM alarms")
    fun getAlarms(): Flow<List<Alarm>>

    @Query("SELECT*FROM alarms WHERE dbId=:id LIMIT 1")
    suspend fun getAlarm(id: Long): Alarm?
}