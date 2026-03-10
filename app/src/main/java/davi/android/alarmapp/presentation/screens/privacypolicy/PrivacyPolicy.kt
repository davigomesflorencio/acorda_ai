package davi.android.alarmapp.presentation.screens.privacypolicy

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
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.Text
import davi.android.alarmapp.R

@Composable
fun PrivacyPolicy() {
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
            autoCentering = null,
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
                    text = stringResource(R.string.privacy_policy_title),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            item {
                Text(
                    text = stringResource(R.string.privacy_policy_content),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Start
                )
            }
        }
    }
}
