package davi.android.alarmapp.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import davi.android.alarmapp.core.DatabaseConstants
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = DatabaseConstants.TABLE_ALARM)
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val dbId: Long = 0,
    @ColumnInfo(name = "vibration")
    var vibration: Boolean,
    @ColumnInfo(name = "repeatDays")
    var repeatDays: String,
    @ColumnInfo(name = "minute")
    var min: Int,
    @ColumnInfo(name = "hour")
    var hour: Int,
    @ColumnInfo(name = "disabled")
    var disabled: Boolean,
    @ColumnInfo(name = "soundActive")
    var soundActive: Boolean,
    @ColumnInfo(name = "snoozeActive")
    var snoozeActive: Boolean,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "ringtone")
    var ringtone: String = "",
) {
    fun formattedTime(): String {
        return hour.toString().padStart(2, '0') + ":" + min.toString().padStart(2, '0')
    }

    override fun hashCode(): Int {
        return dbId.toInt()
    }
}