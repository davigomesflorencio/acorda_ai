package davi.android.alarmapp.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import davi.android.alarmapp.R
import davi.android.alarmapp.core.Constants
import davi.android.alarmapp.data.dao.AlarmDao
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.receivers.AlarmReceiver
import kotlinx.coroutines.launch
import java.util.Calendar

class AlarmsViewModel(
    private val application: Application,
    private val alarmDao: AlarmDao
) : AndroidViewModel(application) {
    private val appApplicationContext: Context = getApplication<Application>().applicationContext
    var alarms = mutableStateOf<List<Alarm>>(emptyList())

    val listDays = arrayListOf(
        appApplicationContext.getString(R.string.mon),
        appApplicationContext.getString(R.string.tue),
        appApplicationContext.getString(R.string.wed),
        appApplicationContext.getString(R.string.thu),
        appApplicationContext.getString(R.string.fri),
        appApplicationContext.getString(R.string.sat),
        appApplicationContext.getString(R.string.sun),
    )

    init {
        viewModelScope.launch {
            alarmDao.getAlarms().collect {
                alarms.value = it
            }
        }
    }

    fun disable(alarm: Alarm) {
        viewModelScope.launch {
            val new = alarm.copy()
            new.disabled = false
            updateAlarm(new)
        }
    }

    fun activateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            val new = alarm.copy()
            new.disabled = true
            updateAlarm(new)
        }
    }

    suspend fun updateAlarm(alarm: Alarm): Boolean {
        try {
            val alarmIdFromDb = alarm.dbId
            alarmDao.update(alarm)
            scheduleAlarmInManager(alarm, alarmIdFromDb.toInt(), alarm.disabled)
            return true
        } catch (_: Exception) {
            return false
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            alarmDao.delete(alarm)
            val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (alarm.repeatDays.isEmpty()) {
                val intent = Intent(application, AlarmReceiver::class.java).apply {
                    action = Constants.NOTIFICATION_INTENT_ACTION_START_ALARM
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    application,
                    alarm.dbId.toInt(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
            } else {
                val selectedDaysList = alarm.repeatDays.split(",")
                selectedDaysList.forEachIndexed { index, dayString ->
                    val dayOfWeek = selectDayOfWeek(dayString)
                    if (dayOfWeek == -1) return@forEachIndexed

                    val requestCodeForRepeating = alarm.dbId.toInt() + dayOfWeek

                    val intent = Intent(application, AlarmReceiver::class.java).apply {
                        action = Constants.NOTIFICATION_INTENT_ACTION_START_ALARM
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        application,
                        requestCodeForRepeating,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    alarmManager.cancel(pendingIntent)
                    pendingIntent.cancel()
                }
            }

            val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(alarm.dbId.toInt())
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleAlarmInManager(alarm: Alarm, uniqueRequestCodeBase: Int, enabled: Boolean) {
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (!enabled) {
            if (alarm.repeatDays.isEmpty()) {
                val intent = Intent(application, AlarmReceiver::class.java).apply {
                    action = Constants.NOTIFICATION_INTENT_ACTION_START_ALARM
                }
                val pendingIntent = PendingIntent.getBroadcast(application, uniqueRequestCodeBase, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel() // Cancelar também o objeto PendingIntent em si
            } else {
                val selectedDaysList = alarm.repeatDays.split(",").filter { it.isNotBlank() }
                selectedDaysList.forEach { dayString ->
                    val dayOfWeek = selectDayOfWeek(dayString)
                    if (dayOfWeek == -1)
                        return@forEach
                    val requestCodeForRepeating = uniqueRequestCodeBase + dayOfWeek // Deve corresponder ao agendamento
                    val intent = Intent(application, AlarmReceiver::class.java).apply {
                        action = Constants.NOTIFICATION_INTENT_ACTION_START_ALARM
                    }
                    val pendingIntent = PendingIntent.getBroadcast(
                        application,
                        requestCodeForRepeating,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )
                    alarmManager.cancel(pendingIntent)
                    pendingIntent.cancel()
                }
            }
            // Opcional: Remover notificação se o alarme estiver desabilitado
            val notificationManager = application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(uniqueRequestCodeBase) // Ou alarm.dbId.toInt() se for consistente
            return
        }

        if (alarm.repeatDays.isEmpty()) {
            // Non-repeating alarm
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarm.hour)
                set(Calendar.MINUTE, alarm.min)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                // Se a hora já passou hoje, agendar para amanhã
                if (before(Calendar.getInstance())) {
                    add(Calendar.DATE, 1)
                    Log.d("AlarmScheduleDebug", "Alarm time ${alarm.hour}:${alarm.min} for ID $uniqueRequestCodeBase is in the past for today, scheduling for tomorrow.")
                }
            }

            val alarmIntent = Intent(application, AlarmReceiver::class.java).apply {
                action = Constants.NOTIFICATION_INTENT_ACTION_START_ALARM
                putExtra(Constants.ALARM_ID_EXTRA, alarm.dbId)
                putExtra(Constants.EXTRA_ALARM_TIME_HOUR, alarm.hour)
                putExtra(Constants.EXTRA_ALARM_TIME_MINUTE, alarm.min)
                putExtra(Constants.EXTRA_ALARM_SOUND_ENABLED, alarm.soundActive)
                putExtra(Constants.EXTRA_ALARM_VIBRATION_ENABLED, alarm.vibration)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                application,
                uniqueRequestCodeBase,
                alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        } else {
            // Repeating alarm
            val selectedDaysList = alarm.repeatDays.split(",").filter { it.isNotBlank() }
            selectedDaysList.forEach { dayString ->
                val dayOfWeek = selectDayOfWeek(dayString)
                if (dayOfWeek == -1)
                    return@forEach
                val calendar: Calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, alarm.hour)
                    set(Calendar.MINUTE, alarm.min)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.DAY_OF_WEEK, dayOfWeek)
                    // Se a hora já passou para este dia da semana, agendar para a próxima semana
                    if (before(Calendar.getInstance())) {
                        add(Calendar.WEEK_OF_YEAR, 1)
                    }
                }

                val requestCodeForRepeating = uniqueRequestCodeBase + dayOfWeek // Crucial para unicidade
                val alarmIntent = Intent(application, AlarmReceiver::class.java).apply {
                    action = Constants.NOTIFICATION_INTENT_ACTION_START_ALARM
                    putExtra(Constants.ALARM_ID_EXTRA, alarm.dbId)
                    putExtra(Constants.EXTRA_ALARM_TIME_HOUR, alarm.hour)
                    putExtra(Constants.EXTRA_ALARM_TIME_MINUTE, alarm.min)
                    putExtra(Constants.EXTRA_ALARM_SOUND_ENABLED, alarm.soundActive)
                    putExtra(Constants.EXTRA_ALARM_VIBRATION_ENABLED, alarm.vibration)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    application,
                    requestCodeForRepeating,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            }
        }
    }

    fun selectDayOfWeek(element: String): Int {
        when (element) {
            "SEG" -> {
                return Calendar.MONDAY
            }

            "TER" -> {
                return Calendar.TUESDAY
            }

            "QUA" -> {
                return Calendar.WEDNESDAY
            }

            "QUI" -> {
                return Calendar.THURSDAY
            }

            "SEX" -> {
                return Calendar.FRIDAY
            }

            "SAB" -> {
                return Calendar.SATURDAY
            }

            "DOM" -> {
                return Calendar.SUNDAY
            }
        }
        return -1
    }

}