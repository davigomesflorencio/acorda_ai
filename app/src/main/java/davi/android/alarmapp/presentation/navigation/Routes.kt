package davi.android.alarmapp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import davi.android.alarmapp.domain.model.Alarm
import kotlinx.serialization.Serializable

@Serializable
data object Alarms : NavKey

@Serializable
data object AddAlarm : NavKey

@Serializable
data object DetailsAlarm : NavKey

@Serializable
data object AlarmDaysWeekScreen : NavKey

@Serializable
data class EditAlarm(val alarm: Alarm) : NavKey

@Serializable
data class EditDetailsAlarm(val alarm: Alarm) : NavKey