package davi.android.alarmapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import davi.android.alarmapp.data.dao.AlarmDao
import davi.android.alarmapp.domain.model.Alarm

@Database(
    entities = [Alarm::class],
    version = 7,
    exportSchema = true
)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun getAlarmDatabaseDao(): AlarmDao
}