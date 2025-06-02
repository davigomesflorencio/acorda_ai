package davi.android.alarmapp.presentation.screens.alarmDayWeeks

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.ToggleButton
import androidx.wear.compose.material.ToggleButtonDefaults
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.EdgeButtonSize
import androidx.wear.compose.material3.ScreenScaffold
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel

@Composable
fun AlarmDaysWeekScreen(
    addAlarmViewModel: AddAlarmViewModel,
    backStack: SnapshotStateList<Any>
) {
    val scope = rememberCoroutineScope()
    val state = rememberScalingLazyListState()
    val horizontalPadding = LocalConfiguration.current.screenWidthDp.dp * 0.052f
    val verticalPadding = LocalConfiguration.current.screenHeightDp.dp * 0.16f

    val toggleBtColor = ToggleButtonDefaults.toggleButtonColors(
        checkedBackgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    )

    var dayDomingo by addAlarmViewModel.dayDomingo
    var daySegunda by addAlarmViewModel.daySegunda
    var dayTerca by addAlarmViewModel.dayTerca
    var dayQuarta by addAlarmViewModel.dayQuarta
    var dayQuinta by addAlarmViewModel.dayQuinta
    var daySexta by addAlarmViewModel.daySexta
    var daySabado by addAlarmViewModel.daySabado

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
                    backStack.removeLastOrNull()
                },
                buttonSize = EdgeButtonSize.ExtraSmall,
                colors = ButtonDefaults.filledVariantButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),

                ) {
                Text("Salvar")
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
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ToggleButton(
                        checked = dayDomingo,
                        onCheckedChange = {
                            dayDomingo = !dayDomingo
                            if (dayDomingo) {
                                addAlarmViewModel.addToSelectedDays("DOM")
                            } else {
                                addAlarmViewModel.removeFromSelectedDays("DOM")
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text("DOM")
                    }
                    ToggleButton(
                        checked = daySegunda,
                        onCheckedChange = {
                            daySegunda = !daySegunda
                            if (daySegunda) {
                                addAlarmViewModel.addToSelectedDays("SEG")
                            } else {
                                addAlarmViewModel.removeFromSelectedDays("SEG")
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text("SEG")
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ToggleButton(
                        checked = dayTerca,
                        onCheckedChange = {
                            dayTerca = !dayTerca
                            if (dayTerca) {
                                addAlarmViewModel.addToSelectedDays("TER")
                            } else {
                                addAlarmViewModel.removeFromSelectedDays("TER")
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text("TER")
                    }
                    ToggleButton(
                        checked = dayQuarta,
                        onCheckedChange = {
                            dayQuarta = !dayQuarta
                            if (dayQuarta) {
                                addAlarmViewModel.addToSelectedDays("QUA")
                            } else {
                                addAlarmViewModel.removeFromSelectedDays("QUA")
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text("QUA")
                    }
                    ToggleButton(
                        checked = dayQuinta,
                        onCheckedChange = {
                            dayQuinta = !dayQuinta
                            if (dayQuinta) {
                                addAlarmViewModel.addToSelectedDays("QUI")
                            } else {
                                addAlarmViewModel.removeFromSelectedDays("QUI")
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text("QUI")
                    }
                }
            }
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    ToggleButton(
                        checked = daySexta,
                        onCheckedChange = {
                            daySexta = !daySexta
                            if (daySexta) {
                                addAlarmViewModel.addToSelectedDays("SEX")
                            } else {
                                addAlarmViewModel.removeFromSelectedDays("SEX")
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text("SEX")
                    }
                    ToggleButton(
                        checked = daySabado,
                        onCheckedChange = {
                            daySabado = !daySabado
                            if (daySabado) {
                                addAlarmViewModel.addToSelectedDays("SAB")
                            } else {
                                addAlarmViewModel.removeFromSelectedDays("SAB")
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text("SAB")
                    }
                }
            }
        }
    }
}