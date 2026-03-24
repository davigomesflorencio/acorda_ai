package davi.android.alarmapp.presentation.viewmodel

import android.app.Application
import android.content.Context
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
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.usecase.SaveAlarmUseCase
import davi.android.alarmapp.domain.usecase.UpdateAlarmUseCase
import davi.android.alarmapp.domain.vibration.ManagerVibrationAndSound
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddAlarmViewModel(
    application: Application,
    private val saveAlarmUseCase: SaveAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
) : AndroidViewModel(application) {

    private val appContext: Context = getApplication<Application>().applicationContext

    val days = mutableStateListOf<String>()
    var minute = mutableIntStateOf(0)
    var hour = mutableIntStateOf(0)

    var addVibration = mutableStateOf(false)
    var addSound = mutableStateOf(false)
    var addSnooze = mutableStateOf(false)

    val managerVibrationAndSound = ManagerVibrationAndSound(appContext)

    val listDays = arrayListOf(
        appContext.getString(R.string.mon),
        appContext.getString(R.string.tue),
        appContext.getString(R.string.wed),
        appContext.getString(R.string.thu),
        appContext.getString(R.string.fri),
        appContext.getString(R.string.sat),
        appContext.getString(R.string.sun),
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

        val calendar = java.util.Calendar.getInstance()
        minute.intValue = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        hour.intValue = calendar.get(java.util.Calendar.MINUTE)

        managerVibrationAndSound.init()

        getDefaultRingtoneUri(RingtoneManager.TYPE_ALARM)?.let { onRingtoneSelected(it) }
    }

    fun onRingtoneSelected(uri: Uri?) {
        _ringtoneUri.value = uri
        ringtoneTitle.value = if (uri != null) {
            getRingtoneTitle(getApplication(), uri)
        } else {
            appContext.getString(R.string.silent)
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
                ringtoneTitle.value = appContext.getString(R.string.error_playing_sound)
                e.printStackTrace()
            }
        }
    }

    fun stopPreview() {
        viewModelScope.launch {
            currentRingtone?.let { if (it.isPlaying) it.stop() }
            currentRingtone = null
        }
    }

    fun getRingtoneTitle(context: Context, uri: Uri): String? {
        return try {
            RingtoneManager.getRingtone(context, uri).getTitle(context)
        } catch (e: Exception) {
            e.printStackTrace()
            context.getString(R.string.unknown_sound)
        }
    }

    private fun getDefaultRingtoneUri(type: Int): Uri? = RingtoneManager.getDefaultUri(type)

    override fun onCleared() {
        super.onCleared()
        stopPreview()
    }

    fun addToSelectedDays(day: String) {
        if (!days.contains(day)) days.add(day)
    }

    fun removeFromSelectedDays(day: String) {
        days.remove(day)
    }

    suspend fun saveAlarm(): Boolean {
        return try {
            val alarm = Alarm(
                dbId = 0,
                vibration = addVibration.value,
                repeatDays = days.joinToString(separator = ","),
                min = minute.intValue,
                hour = hour.intValue,
                disabled = true,
                soundActive = addSound.value,
                snoozeActive = addSnooze.value,
                ringtone = ringtoneUri.value.toString()
            )
            saveAlarmUseCase(alarm)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun updateAlarm(alarm: Alarm): Boolean {
        return try {
            alarm.vibration = addVibration.value
            alarm.repeatDays = days.joinToString(separator = ",")
            alarm.min = minute.intValue
            alarm.hour = hour.intValue
            alarm.ringtone = ringtoneUri.value.toString()
            alarm.disabled = true
            alarm.soundActive = addSound.value
            alarm.snoozeActive = addSnooze.value
            updateAlarmUseCase(alarm)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}