package davi.android.alarmapp.presentation.viewmodel

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import davi.android.alarmapp.R
import davi.android.alarmapp.core.Constants
import davi.android.alarmapp.data.dao.AlarmDao
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.vibration.ManagerVibrationAndSound
import davi.android.alarmapp.receivers.AlarmReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

class AddAlarmViewModel(private val application: Application, private val alarmDao: AlarmDao) : AndroidViewModel(application) {
    private val appApplicationContext: Context = getApplication<Application>().applicationContext
    val days = mutableStateListOf<String>() // Changed to mutableStateListOf
    var minute = mutableIntStateOf(0)
    var hour = mutableIntStateOf(0)

    var addVibration = mutableStateOf(false)
    var addSound = mutableStateOf(false)
    var addSnooze = mutableStateOf(false)

    val managerVibrationAndSound = ManagerVibrationAndSound(appApplicationContext)

    val listDays = arrayListOf(
        appApplicationContext.getString(R.string.mon),
        appApplicationContext.getString(R.string.tue),
        appApplicationContext.getString(R.string.wed),
        appApplicationContext.getString(R.string.thu),
        appApplicationContext.getString(R.string.fri),
        appApplicationContext.getString(R.string.sat),
        appApplicationContext.getString(R.string.sun),
    )

    var dayDomingo = mutableStateOf(false)
    var daySegunda = mutableStateOf(false)
    var dayTerca = mutableStateOf(false)
    var dayQuarta = mutableStateOf(false)
    var dayQuinta = mutableStateOf(false)
    var daySexta = mutableStateOf(false)
    var daySabado = mutableStateOf(false)

    private val _ringtoneUri = MutableStateFlow<Uri?>(null)
    val ringtoneUri: StateFlow<Uri?> = _ringtoneUri.asStateFlow()

    val ringtoneTitle: MutableState<String?> = mutableStateOf("")

    private var currentRingtone: Ringtone? = null

    init {
        addSound.value = false

        val calendar = Calendar.getInstance()
        minute.intValue = calendar.get(Calendar.HOUR_OF_DAY)
        hour.intValue = calendar.get(Calendar.MINUTE)

        managerVibrationAndSound.init()

        getDefaultRingtoneUri(RingtoneManager.TYPE_ALARM)?.let { onRingtoneSelected(it) }
    }

    fun onRingtoneSelected(uri: Uri?) {
        _ringtoneUri.value = uri
        if (uri != null) {
            ringtoneTitle.value = getRingtoneTitle(getApplication(), uri)
        } else {
            ringtoneTitle.value = appApplicationContext.getString(R.string.silent)
        }
        stopPreview()
    }

    fun playPreview(uri: Uri?) {
        if (uri == null) return
        stopPreview()
        viewModelScope.launch {
            try {
                currentRingtone = RingtoneManager.getRingtone(getApplication(), uri)
                currentRingtone?.play()
            } catch (e: Exception) {
                ringtoneTitle.value = appApplicationContext.getString(R.string.error_playing_sound)
                e.printStackTrace()
            }
        }
    }

    fun stopPreview() {
        viewModelScope.launch {
            currentRingtone?.let {
                if (it.isPlaying)
                    it.stop()
            }
            currentRingtone = null
        }
    }

    fun getRingtoneTitle(context: Context, uri: Uri): String? {
        return try {
            val ringtone = RingtoneManager.getRingtone(context, uri)
            val title = ringtone.getTitle(context)
            title
        } catch (e: Exception) {
            e.printStackTrace()
            context.getString(R.string.unknown_sound)
        }
    }

    private fun getDefaultRingtoneUri(type: Int): Uri? {
        return RingtoneManager.getDefaultUri(type)
    }

    override fun onCleared() {
        super.onCleared()
        stopPreview()
    }


    fun addToSelectedDays(day: String) {
        if (!days.contains(day)) {
            days.add(day)
        }
    }

    fun removeFromSelectedDays(day: String) {
        if (days.contains(day)) {
            days.remove(day)
        }
    }

    suspend fun updateAlarm(alarm: Alarm): Boolean {
        try {
            alarm.vibration = addVibration.value
            alarm.repeatDays = days.joinToString(separator = ",") { it }
            alarm.min = minute.intValue
            alarm.hour = hour.intValue
            alarm.ringtone = ringtoneUri.value.toString()
            alarm.disabled = true
            alarm.soundActive = addSound.value
            alarm.snoozeActive = addSnooze.value

            val alarmIdFromDb = alarm.dbId
            alarmDao.update(alarm)
            scheduleAlarmInManager(alarm, alarmIdFromDb.toInt())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    suspend fun saveAlarm(): Boolean {
        try {
            val alarm = Alarm(
                dbId = 0,
                vibration = addVibration.value,
                repeatDays = days.joinToString(separator = ",") { it },
                min = minute.intValue,
                hour = hour.intValue,
                disabled = true,
                soundActive = addSound.value,
                snoozeActive = addSnooze.value,
                ringtone = ringtoneUri.value.toString()
            )
            val alarmIdFromDb = alarmDao.insert(alarm)
            val alarmToSchedule = alarm.copy(dbId = alarmIdFromDb)
            scheduleAlarmInManager(alarmToSchedule, alarmIdFromDb.toInt())
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleAlarmInManager(alarm: Alarm, uniqueRequestCodeBase: Int) {
        val alarmManager = application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if (alarm.repeatDays.isEmpty()) {
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarm.hour)
                set(Calendar.MINUTE, alarm.min)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
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
            val selectedDaysList = alarm.repeatDays.split(",")
            selectedDaysList.forEachIndexed { _, dayString ->
                val dayOfWeek = selectDayOfWeek(dayString)
                if (dayOfWeek == -1) return@forEachIndexed

                val calendar: Calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, alarm.hour)
                    set(Calendar.MINUTE, alarm.min)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.DAY_OF_WEEK, dayOfWeek)
                }

                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1)
                }

                val requestCodeForRepeating = uniqueRequestCodeBase + dayOfWeek

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


    fun selectDayOfWeek(day: String): Int {
        return when (day) {
            appApplicationContext.getString(R.string.sun) -> Calendar.SUNDAY
            appApplicationContext.getString(R.string.mon) -> Calendar.MONDAY
            appApplicationContext.getString(R.string.tue) -> Calendar.TUESDAY
            appApplicationContext.getString(R.string.wed) -> Calendar.WEDNESDAY
            appApplicationContext.getString(R.string.thu) -> Calendar.THURSDAY
            appApplicationContext.getString(R.string.fri) -> Calendar.FRIDAY
            appApplicationContext.getString(R.string.sat) -> Calendar.SATURDAY
            else -> -1
        }
    }

}
