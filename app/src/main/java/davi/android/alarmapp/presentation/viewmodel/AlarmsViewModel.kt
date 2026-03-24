package davi.android.alarmapp.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import davi.android.alarmapp.R
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.domain.usecase.DeleteAlarmUseCase
import davi.android.alarmapp.domain.usecase.GetAlarmsUseCase
import davi.android.alarmapp.domain.usecase.ToggleAlarmUseCase
import davi.android.alarmapp.domain.usecase.UpdateAlarmUseCase
import kotlinx.coroutines.launch

class AlarmsViewModel(
    application: Application,
    private val getAlarmsUseCase: GetAlarmsUseCase,
    private val toggleAlarmUseCase: ToggleAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val deleteAlarmUseCase: DeleteAlarmUseCase,
) : AndroidViewModel(application) {

    private val appContext: Context = getApplication<Application>().applicationContext

    var alarms = mutableStateOf<List<Alarm>>(emptyList())

    val listDays = arrayListOf(
        appContext.getString(R.string.mon),
        appContext.getString(R.string.tue),
        appContext.getString(R.string.wed),
        appContext.getString(R.string.thu),
        appContext.getString(R.string.fri),
        appContext.getString(R.string.sat),
        appContext.getString(R.string.sun),
    )

    init {
        viewModelScope.launch {
            getAlarmsUseCase().collect {
                alarms.value = it
            }
        }
    }

    fun disable(alarm: Alarm) {
        viewModelScope.launch {
            toggleAlarmUseCase(alarm, active = false)
        }
    }

    fun activateAlarm(alarm: Alarm) {
        viewModelScope.launch {
            toggleAlarmUseCase(alarm, active = true)
        }
    }

    suspend fun updateAlarm(alarm: Alarm): Boolean {
        return try {
            updateAlarmUseCase(alarm)
            true
        } catch (_: Exception) {
            false
        }
    }

    fun deleteAlarm(alarm: Alarm) {
        viewModelScope.launch {
            deleteAlarmUseCase(alarm)
        }
    }
}