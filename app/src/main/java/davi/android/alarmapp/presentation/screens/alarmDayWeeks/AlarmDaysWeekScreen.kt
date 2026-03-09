package davi.android.alarmapp.presentation.screens.alarmDayWeeks

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.RoundedPolygon
import androidx.lifecycle.viewmodel.compose.viewModel
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
import davi.android.alarmapp.presentation.navigation.RouteSplash
import davi.android.alarmapp.presentation.theme.AlarmAppTheme
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AlarmDaysWeekScreen(
    addAlarmViewModel: AddAlarmViewModel = viewModel(),
    backStack: SnapshotStateList<Any>
) {
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

    val sunString = stringResource(R.string.sun)
    val monString = stringResource(R.string.mon)
    val tueString = stringResource(R.string.tue)
    val wedString = stringResource(R.string.wed)
    val thuString = stringResource(R.string.thu)
    val friString = stringResource(R.string.fri)
    val satString = stringResource(R.string.sat)

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
                                addAlarmViewModel.addToSelectedDays(sunString)
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(sunString)
                            }
                        },
                        shape = RoundedPolygon(MaterialShapes.Cookie6Sided).toShape(0),
                        colors = toggleBtColor
                    ) {
                        Text(
                            sunString,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    ToggleButton(
                        checked = daySegunda,
                        onCheckedChange = {
                            daySegunda = !daySegunda
                            if (daySegunda) {
                                addAlarmViewModel.addToSelectedDays(monString)
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(monString)
                            }
                        },
                        shape = RoundedPolygon(MaterialShapes.Cookie6Sided).toShape(0),
                        colors = toggleBtColor
                    ) {
                        Text(
                            monString,
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
                                addAlarmViewModel.addToSelectedDays(tueString)
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(tueString)
                            }
                        },
                        shape = RoundedPolygon(MaterialShapes.Cookie6Sided).toShape(0),
                        colors = toggleBtColor
                    ) {
                        Text(
                            tueString,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    ToggleButton(
                        checked = dayQuarta,
                        onCheckedChange = {
                            dayQuarta = !dayQuarta
                            if (dayQuarta) {
                                addAlarmViewModel.addToSelectedDays(wedString)
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(wedString)
                            }
                        },
                        shape = RoundedPolygon(MaterialShapes.Cookie6Sided).toShape(0),
                        colors = toggleBtColor
                    ) {
                        Text(
                            wedString,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    ToggleButton(
                        checked = dayQuinta,
                        onCheckedChange = {
                            dayQuinta = !dayQuinta
                            if (dayQuinta) {
                                addAlarmViewModel.addToSelectedDays(thuString)
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(thuString)
                            }
                        },
                        shape = RoundedPolygon(MaterialShapes.Cookie6Sided).toShape(0),
                        colors = toggleBtColor
                    ) {
                        Text(
                            thuString,
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
                                addAlarmViewModel.addToSelectedDays(friString)
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(friString)
                            }
                        },
                        shape = RoundedPolygon(MaterialShapes.Cookie6Sided).toShape(0),
                        colors = toggleBtColor
                    ) {
                        Text(
                            friString,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    ToggleButton(
                        checked = daySabado,
                        onCheckedChange = {
                            daySabado = !daySabado
                            if (daySabado) {
                                addAlarmViewModel.addToSelectedDays(satString)
                            } else {
                                addAlarmViewModel.removeFromSelectedDays(satString)
                            }
                        },
                        shape = RoundedPolygon(MaterialShapes.Cookie6Sided).toShape(0),
                        colors = toggleBtColor
                    ) {
                        Text(
                            satString,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAlarmDaysWeekScreen() {
    val backStack = remember { mutableStateListOf<Any>(RouteSplash) }
    AlarmAppTheme {
        AlarmDaysWeekScreen(backStack = backStack)
    }
}