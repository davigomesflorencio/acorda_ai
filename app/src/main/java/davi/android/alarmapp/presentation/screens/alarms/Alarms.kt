package davi.android.alarmapp.presentation.screens.alarms

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.EdgeButtonSize
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SwitchButtonDefaults
import androidx.wear.compose.material3.Text
import davi.android.alarmapp.presentation.navigation.AddAlarm
import davi.android.alarmapp.presentation.viewmodel.AlarmsViewModel

@Composable
fun Alarms(
    alarmsViewModel: AlarmsViewModel,
    backStack: SnapshotStateList<Any>
) {
    val context = LocalContext.current
    val state = rememberScalingLazyListState()
    val horizontalPadding = LocalConfiguration.current.screenWidthDp.dp * 0.052f
    val verticalPadding = LocalConfiguration.current.screenHeightDp.dp * 0.16f

    val alarmsScheduled by alarmsViewModel.alarms

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
        }
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    // State to track permission status
    var hasPermission by remember { mutableStateOf(alarmManager.canScheduleExactAlarms()) }

    // Listen for permission changes
    val permissionListener = remember {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                hasPermission = alarmManager.canScheduleExactAlarms()
            }
        }
    }

    // Register receiver
    DisposableEffect(Unit) {
        context.registerReceiver(
            permissionListener,
            IntentFilter(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
        )
        onDispose {
            context.unregisterReceiver(permissionListener)
        }
    }

    LaunchedEffect(Unit) {
        val activity = context as? Activity // Or obtain activity differently
        if (activity?.shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) == false) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    val customSplitSwitchColors = SwitchButtonDefaults.splitSwitchButtonColors(
        checkedContainerColor = MaterialTheme.colorScheme.secondary,
        uncheckedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        checkedContentColor = Color.White,
        uncheckedContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        checkedSplitContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
        uncheckedSplitContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        checkedThumbIconColor = Color.White,
        checkedThumbColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
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
                    backStack.add(AddAlarm)
                },
                buttonSize = EdgeButtonSize.Small,
                colors = ButtonDefaults.filledVariantButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                enabled = true
            ) {
                Text(
                    "Adicionar",
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(alarmsScheduled) { index, item ->
                AlarmItem(alarmsViewModel, backStack, item, customSplitSwitchColors)
            }
        }
    }
}