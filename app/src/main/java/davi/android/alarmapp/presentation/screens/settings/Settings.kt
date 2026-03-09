package davi.android.alarmapp.presentation.screens.settings

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ButtonDefaults
import androidx.wear.compose.material3.ScreenScaffold
import davi.android.alarmapp.R
import davi.android.alarmapp.presentation.navigation.RouteAbout
import davi.android.alarmapp.presentation.navigation.RoutePrivacyPolicy
import davi.android.alarmapp.presentation.navigation.RouteTermsOfUse

@Composable
fun Settings(
    backStack: SnapshotStateList<Any>
) {
    val context = LocalContext.current
    val state = rememberScalingLazyListState()

    ScreenScaffold(
        scrollState = state
    ) { contentPadding ->
        ScalingLazyColumn(
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
                    text = stringResource(R.string.settings_title),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )
            }
            item {
                Button(
                    onClick = { backStack.add(RouteAbout) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Config",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.about_title))
                }
            }
            item {
                Button(
                    onClick = { backStack.add(RouteTermsOfUse) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Icon(
                        painterResource(R.drawable.ic_contract), // Assuming you have an ic_contract drawable
                        contentDescription = "Icon terms of use",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.terms_of_use))
                }
            }
            item {
                Button(
                    onClick = { backStack.add(RoutePrivacyPolicy) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Icon(
                        Icons.Default.PrivacyTip,
                        contentDescription = "Icon privacy policy",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.privacy_policy))
                }
            }
            item {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    onClick = {
                        // App version action, if any needed
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Icon(
                        Icons.Default.Apps,
                        contentDescription = "App version",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(R.string.app_version, "1.0.0"), // Replace "1.0.0" with BuildConfig.VERSION_NAME if available
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        // Intent to open the app's page in the Play Store
                        val uri = ("market://details?id=" + context.packageName).toUri()
                        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                        goToMarket.addFlags(
                            Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                        )
                        try {
                            context.startActivity(goToMarket)
                        } catch (e: Exception) {
                            val browserIntent = Intent(
                                Intent.ACTION_VIEW, ("http://play.google.com/store/apps/details?id=" + context.packageName).toUri()
                            )
                            context.startActivity(browserIntent)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        Icons.Default.StarRate,
                        contentDescription = "Rate app",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(stringResource(R.string.rate_app))
                }
            }
            item {
                Button(
                    onClick = {
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/davigomesflorencio"))
//                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        painterResource(R.drawable.ic_github), // Assuming you have an ic_github drawable
                        contentDescription = "Icon Github",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        stringResource(R.string.github), // Assuming you have a github string resource
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            item {
                Button(
                    onClick = {
//                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://davigomesflorencio.com.br/"))
//                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                ) {
                    Icon(
                        painterResource(R.drawable.ic_web), // Assuming you have an ic_web drawable
                        contentDescription = "Icon Portfolio",
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        stringResource(R.string.portfolio), // Assuming you have a portfolio string resource
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}