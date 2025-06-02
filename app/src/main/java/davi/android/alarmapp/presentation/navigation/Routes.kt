package davi.android.alarmapp.presentation.navigation

import davi.android.alarmapp.domain.model.Alarm

data object Alarms
data object AddAlarm
data object DetailsAlarm
data object AlarmDaysWeekScreen
data class EditAlarm(val alarm: Alarm)
data class EditDetailsAlarm(val alarm: Alarm)