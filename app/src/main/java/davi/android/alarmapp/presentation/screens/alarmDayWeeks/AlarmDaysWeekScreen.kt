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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import davi.android.alarmapp.R
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel

@Composable
fun AlarmDaysWeekScreen(
    addAlarmViewModel: AddAlarmViewModel,
    backStack: SnapshotStateList<Any>
) {
    val context = LocalContext.current
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
                Text(stringResource(R.string.save))
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
                                addAlarmViewModel.addToSelectedDays(context.getString(R.string.sun))
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(context.getString(R.string.sun))
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text(
                            context.getString(R.string.sun),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    ToggleButton(
                        checked = daySegunda,
                        onCheckedChange = {
                            daySegunda = !daySegunda
                            if (daySegunda) {
                                addAlarmViewModel.addToSelectedDays(context.getString(R.string.mon))
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(context.getString(R.string.mon))
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text(
                            context.getString(R.string.mon),
                            style = MaterialTheme.typography.labelSmall
                        )
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
                                addAlarmViewModel.addToSelectedDays(context.getString(R.string.tue))
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(context.getString(R.string.tue))
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text(
                            context.getString(R.string.tue),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    ToggleButton(
                        checked = dayQuarta,
                        onCheckedChange = {
                            dayQuarta = !dayQuarta
                            if (dayQuarta) {
                                addAlarmViewModel.addToSelectedDays(context.getString(R.string.wed))
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(context.getString(R.string.wed))
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text(
                            context.getString(R.string.wed),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    ToggleButton(
                        checked = dayQuinta,
                        onCheckedChange = {
                            dayQuinta = !dayQuinta
                            if (dayQuinta) {
                                addAlarmViewModel.addToSelectedDays(context.getString(R.string.thu))
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(context.getString(R.string.thu))
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text(
                            context.getString(R.string.thu),
                            style = MaterialTheme.typography.labelSmall
                        )
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
                                addAlarmViewModel.addToSelectedDays(context.getString(R.string.fri))
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(context.getString(R.string.fri))
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text(
                            context.getString(R.string.fri),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    ToggleButton(
                        checked = daySabado,
                        onCheckedChange = {
                            daySabado = !daySabado
                            if (daySabado) {
                                addAlarmViewModel.addToSelectedDays(context.getString(R.string.sat))
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(context.getString(R.string.sat))
                            }
                        },
                        colors = toggleBtColor
                    ) {
                        Text(
                            context.getString(R.string.sat),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}