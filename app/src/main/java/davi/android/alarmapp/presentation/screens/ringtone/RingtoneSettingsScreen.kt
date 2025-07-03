package davi.android.alarmapp.presentation.screens.ringtone

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.EdgeButton
import androidx.wear.compose.material3.EdgeButtonSize
import androidx.wear.compose.material3.Icon
import androidx.wear.compose.material3.IconButton
import androidx.wear.compose.material3.IconButtonDefaults
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import davi.android.alarmapp.R
import davi.android.alarmapp.presentation.viewmodel.AddAlarmViewModel

@Composable
fun RingtoneSettingsScreen(
    viewModel: AddAlarmViewModel,
    backStack: SnapshotStateList<Any>
) {
    val context = LocalContext.current
    val state = rememberScalingLazyListState()
    val horizontalPadding = LocalConfiguration.current.screenWidthDp.dp * 0.052f
    val verticalPadding = LocalConfiguration.current.screenHeightDp.dp * 0.16f
    val selectedRingtoneUri by viewModel.ringtoneUri.collectAsState()
    val ringtoneTitle by viewModel.ringtoneTitle
    var isPlayingPreview by remember { mutableStateOf(false) }

    val ringtonePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            viewModel.onRingtoneSelected(uri) // ViewModel handles nullability and fetching title
        }
    }

    fun openRingtonePicker() {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
            putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM) // Or TYPE_NOTIFICATION
            putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, context.getString(R.string.select_alarm_sound))
            putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, selectedRingtoneUri)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
            putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
        }
        ringtonePickerLauncher.launch(intent)
    }

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
                buttonSize = EdgeButtonSize.Small,
                colors = ButtonDefaults.filledVariantButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
            ) {
                Text(
                    stringResource(R.string.select),
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall
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
            item {
                Text(
                    text = stringResource(R.string.alarm_ringtone),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            item {
                Text(
                    text = ringtoneTitle ?: stringResource(R.string.silent_or_none), // Display title or fallback
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { openRingtonePicker() },
                        modifier = Modifier.fillMaxWidth(.7f),
                        colors = ButtonDefaults.filledVariantButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MusicNote,
                                contentDescription = stringResource(R.string.description_change_sound),
                                tint = Color.Black
                            )
                            Text(
                                stringResource(R.string.selected_ringtone),
                                color = Color.Black,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    Spacer(Modifier.width(2.dp))
                    if (selectedRingtoneUri != null) { // Only show play/stop if a ringtone is selected
                        IconButton(
                            onClick = {
                                if (isPlayingPreview) {
                                    viewModel.stopPreview()
                                    isPlayingPreview = false
                                } else {
                                    viewModel.playPreview(selectedRingtoneUri)
                                    isPlayingPreview = true
                                }
                            },
                            enabled = selectedRingtoneUri != null,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            if (isPlayingPreview) {
                                Icon(
                                    imageVector = Icons.Default.Stop,
                                    contentDescription = stringResource(R.string.description_stop_preview),
                                    tint = Color.Black
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = stringResource(R.string.description_play_preview),
                                    tint = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}