package davi.android.alarmapp.domain.scheduler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import davi.android.alarmapp.core.Constants
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.receivers.AlarmReceiver
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun schedule(alarm: Alarm) {
        val requestCode = alarm.dbId.toInt()

        if (alarm.repeatDays.isEmpty()) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarm.hour)
                set(Calendar.MINUTE, alarm.min)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                    Log.d("AlarmScheduler", "Alarm ${alarm.hour}:${alarm.min} id=$requestCode is past, scheduling for tomorrow.")
                }
            }
            val pendingIntent = buildPendingIntent(alarm, requestCode)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            alarm.repeatDays.split(",").filter { it.isNotBlank() }.forEach { dayString ->
                val dayOfWeek = dayOfWeekFor(dayString)
                if (dayOfWeek == -1) return@forEach

                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, alarm.hour)
                    set(Calendar.MINUTE, alarm.min)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.DAY_OF_WEEK, dayOfWeek)
                    if (before(Calendar.getInstance())) {
                        add(Calendar.WEEK_OF_YEAR, 1)
                    }
                }
                val repeatingRequestCode = requestCode + dayOfWeek
                val pendingIntent = buildPendingIntent(alarm, repeatingRequestCode)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }

    fun cancel(alarm: Alarm) {
        val requestCode = alarm.dbId.toInt()

        if (alarm.repeatDays.isEmpty()) {
            val pendingIntent = buildCancelPendingIntent(requestCode)
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        } else {
            alarm.repeatDays.split(",").filter { it.isNotBlank() }.forEach { dayString ->
                val dayOfWeek = dayOfWeekFor(dayString)
                if (dayOfWeek == -1) return@forEach
                val repeatingRequestCode = requestCode + dayOfWeek
                val pendingIntent = buildCancelPendingIntent(repeatingRequestCode)
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            }
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(requestCode)
    }

    private fun buildPendingIntent(alarm: Alarm, requestCode: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.NOTIFICATION_INTENT_ACTION_START_ALARM
            putExtra(Constants.ALARM_ID_EXTRA, alarm.dbId)
            putExtra(Constants.EXTRA_ALARM_TIME_HOUR, alarm.hour)
            putExtra(Constants.EXTRA_ALARM_TIME_MINUTE, alarm.min)
            putExtra(Constants.EXTRA_ALARM_SOUND_ENABLED, alarm.soundActive)
            putExtra(Constants.EXTRA_ALARM_VIBRATION_ENABLED, alarm.vibration)
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildCancelPendingIntent(requestCode: Int): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = Constants.NOTIFICATION_INTENT_ACTION_START_ALARM
        }
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun dayOfWeekFor(element: String): Int = when (element) {
        "SEG" -> Calendar.MONDAY
        "TER" -> Calendar.TUESDAY
        "QUA" -> Calendar.WEDNESDAY
        "QUI" -> Calendar.THURSDAY
        "SEX" -> Calendar.FRIDAY
        "SAB" -> Calendar.SATURDAY
        "DOM" -> Calendar.SUNDAY
        else -> -1
    }
}