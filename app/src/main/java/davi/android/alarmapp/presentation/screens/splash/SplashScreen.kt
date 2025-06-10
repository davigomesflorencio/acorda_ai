package davi.android.alarmapp.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Scaffold
import androidx.wear.tooling.preview.devices.WearDevices.SMALL_ROUND
import davi.android.alarmapp.R
import davi.android.alarmapp.presentation.navigation.RouteAlarms
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun SplashScreen(backStack: SnapshotStateList<Any>) {
    LaunchedEffect(Unit) {
        delay(2.seconds)
        backStack.clear()
        backStack.add(RouteAlarms)
    }

    Scaffold(
        Modifier.background(color = Color.Black),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.ic_logo),
                contentDescription = "",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Preview(device = SMALL_ROUND)
@Composable
fun preview(){
    SplashScreen(backStack = SnapshotStateList())
}