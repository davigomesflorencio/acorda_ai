package davi.android.alarmapp.presentation.navigation

import androidx.navigation3.runtime.NavKey
import davi.android.alarmapp.domain.model.Alarm
import kotlinx.serialization.Serializable

@Serializable
data object RouteAlarms : NavKey

@Serializable
data object RouteAddAlarm : NavKey

@Serializable
data object RouteDetailsAlarm : NavKey

@Serializable
data object AlarmDaysWeekScreen : NavKey

@Serializable
data class RouteEditAlarm(val alarm: Alarm) : NavKey

@Serializable
data class RouteEditDetailsAlarm(val alarm: Alarm) : NavKey