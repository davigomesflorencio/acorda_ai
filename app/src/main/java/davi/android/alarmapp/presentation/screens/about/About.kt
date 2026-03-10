package davi.android.alarmapp.presentation.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import androidx.wear.tooling.preview.devices.WearDevices
import davi.android.alarmapp.R
import davi.android.alarmapp.presentation.theme.AlarmAppTheme

@Composable
fun About() {
    val state = rememberScalingLazyListState()

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        ScalingLazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .selectableGroup(),
            autoCentering = AutoCenteringParams(itemIndex = 0),
            contentPadding = contentPadding,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Image(
                    painterResource(R.drawable.ic_logo),
                    contentDescription = "Image",
                    modifier = Modifier.size(64.dp)
                )
            }
            item {
                Text(
                    text = stringResource(R.string.about_title),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
            item {
                Text(
                    text = stringResource(R.string.about_description), // Create this string resource
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(
    device = WearDevices.SMALL_ROUND,
    showBackground = true
)
@Composable
fun PreviewAbout() {
    AlarmAppTheme {
        About()
    }
}