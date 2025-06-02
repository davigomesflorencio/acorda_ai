package davi.android.alarmapp.presentation.screens.editDetailsAlarm

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.EdgeButtonSize
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SplitSwitchButton
import androidx.wear.compose.material3.SwitchButtonDefaults
import androidx.wear.compose.material3.Text
import davi.android.alarmapp.domain.model.Alarm
import davi.android.alarmapp.presentation.navigation.AlarmDaysWeekScreen
import davi.android.alarmapp.presentation.screens.detailsAlarm.DayOfWeekIcon
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel
import kotlinx.coroutines.launch

@Composable
fun EditDetailsAlarm(addAlarmViewModel: AddAlarmViewModel, backStack: SnapshotStateList<Any>, alarm: Alarm) {
    val scope = rememberCoroutineScope()
    val state = rememberScalingLazyListState()
    val horizontalPadding = LocalConfiguration.current.screenWidthDp.dp * 0.052f
    val verticalPadding = LocalConfiguration.current.screenHeightDp.dp * 0.16f

    var soundActive by remember { mutableStateOf(alarm.soundActive) }
    var alarmVibrate by remember { mutableStateOf(alarm.vibration) }
    var sonecaActive by remember { mutableStateOf(false) }

    val customSplitSwitchColors = SwitchButtonDefaults.splitSwitchButtonColors(
        checkedContainerColor = MaterialTheme.colorScheme.secondary,
        uncheckedContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.5f),
        checkedContentColor = Color.Black,
        uncheckedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        checkedSplitContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        uncheckedSplitContainerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.2f),
        checkedThumbIconColor = Color.White,
        checkedThumbColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        checkedSecondaryContentColor = Color.Black,
        uncheckedSecondaryContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        checkedTrackColor = MaterialTheme.colorScheme.onSecondaryContainer,
        checkedTrackBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )

    ScreenScaffold(
        scrollState = state,
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = verticalPadding),
        edgeButton = {
            EdgeButton(
                modifier = Modifier.scrollable(
                    state,
                    orientation = Orientation.Vertical,
                    reverseDirection = true,
                    overscrollEffect = rememberOverscrollEffect()
                ),
                onClick = {
                    addAlarmViewModel.addVibration.value = alarmVibrate
                    addAlarmViewModel.addSound.value = soundActive
                    scope.launch {
                        addAlarmViewModel.saveAlarm()
                    }
                    backStack.removeLastOrNull()
                    backStack.removeLastOrNull()
                },
                buttonSize = EdgeButtonSize.Small,
                colors = ButtonDefaults.filledVariantButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                enabled = true
            ) {
                Text(
                    "Salvar",
                    color = Color.White
                )
            }
        })
    { contentPadding ->
        ScalingLazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .selectableGroup(),
            autoCentering = null,
            contentPadding = contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    "Personalizar Alarme",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item {
                Card(modifier = Modifier.fillMaxSize()) {
                    Chip(
                        onClick = {
                            backStack.add(AlarmDaysWeekScreen)
                        },
                        label = {
                            Text("Repetir", fontSize = 14.sp, color = Color.Black)
                        },
                        secondaryLabel = {
                            if (addAlarmViewModel.days.isEmpty())
                                Text(
                                    "Desligado",
                                    fontSize = 14.sp, color = Color.Black
                                )
                            else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 5.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp), // Space between day icons
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        addAlarmViewModel.listDays.subList(0, 4).forEachIndexed { index, initial ->
                                            DayOfWeekIcon(
                                                dayInitial = initial.substring(0, 1),
                                                isSelected = addAlarmViewModel.days.contains(initial)
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(3.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp), // Space between day icons
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        addAlarmViewModel.listDays.subList(4, 7).forEachIndexed { index, initial ->
                                            DayOfWeekIcon(
                                                dayInitial = initial.substring(0, 1),
                                                isSelected = addAlarmViewModel.days.contains(initial)
                                            )
                                        }
                                    }
                                }
                            }

                        },
                        colors = ChipDefaults.chipColors(
                            backgroundColor = MaterialTheme.colorScheme.secondary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    HorizontalDivider()
                    SplitSwitchButton(
                        label = {
                            Text("Som", fontSize = 14.sp)
                        },
                        checked = soundActive,
                        onCheckedChange = {
                            soundActive = it
                        },
                        toggleContentDescription = "Split Switch Button Sample",
                        onContainerClick = {
                        },
                        enabled = true,
                        colors = customSplitSwitchColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    HorizontalDivider()
                    SplitSwitchButton(
                        label = {
                            Text("Vibração", fontSize = 14.sp)
                        },
                        checked = alarmVibrate,
                        onCheckedChange = {
                            alarmVibrate = it
                        },
                        toggleContentDescription = "Split Switch Button Sample",
                        onContainerClick = {
                        },
                        enabled = true,
                        colors = customSplitSwitchColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                    HorizontalDivider()
                    SplitSwitchButton(
                        label = {
                            Text("Soneca", fontSize = 14.sp)
                        },
                        secondaryLabel = {
                            Text("5 minutos, 3 vezes", fontSize = 12.sp)
                        },
                        checked = sonecaActive,
                        onCheckedChange = {
                            sonecaActive = it
                        },
                        toggleContentDescription = "Split Switch Button Sample",
                        onContainerClick = {
                        },
                        enabled = true,
                        colors = customSplitSwitchColors,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}