package davi.android.alarmapp.presentation.screens.splash

import android.app.Application
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import davi.android.alarmapp.presentation.navigation.RouteAlarms
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], application = Application::class)
class SplashScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun splashScreen_telaRenderizaSemErros() {
        val backStack = SnapshotStateList<Any>()
        composeTestRule.setContent {
            SplashScreen(backStack = backStack)
        }
        composeTestRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun splashScreen_iniciaComBackstackVazia() {
        val backStack = SnapshotStateList<Any>()
        composeTestRule.setContent {
            SplashScreen(backStack = backStack)
        }
        assertTrue(backStack.isEmpty())
    }

    @Test
    fun splashScreen_aposDelayAdicionaRouteAlarmsNoBackstack() {
        val backStack = SnapshotStateList<Any>()
        composeTestRule.setContent {
            SplashScreen(backStack = backStack)
        }
        composeTestRule.waitUntil(timeoutMillis = 4_000L) {
            backStack.isNotEmpty()
        }
        assertTrue(backStack.contains(RouteAlarms))
    }

    @Test
    fun splashScreen_aposDelayBackstackContemApenasRouteAlarms() {
        val backStack = SnapshotStateList<Any>()
        backStack.add("anterior")
        composeTestRule.setContent {
            SplashScreen(backStack = backStack)
        }
        composeTestRule.waitUntil(timeoutMillis = 4_000L) {
            backStack.isNotEmpty() && backStack.contains(RouteAlarms)
        }
        assertFalse(backStack.contains("anterior"))
        assertTrue(backStack.contains(RouteAlarms))
    }
}
