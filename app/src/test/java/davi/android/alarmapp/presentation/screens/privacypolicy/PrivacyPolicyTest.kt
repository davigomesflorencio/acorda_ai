package davi.android.alarmapp.presentation.screens.privacypolicy

import android.app.Application
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], application = Application::class)
class PrivacyPolicyTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun privacyPolicy_telaRenderizaSemErros() {
        composeTestRule.setContent {
            PrivacyPolicy()
        }
        composeTestRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun privacyPolicy_imagemLogoExibida() {
        composeTestRule.setContent {
            PrivacyPolicy()
        }
        composeTestRule.onNodeWithContentDescription("Image").assertIsDisplayed()
    }

    @Test
    fun privacyPolicy_tituloExibido() {
        composeTestRule.setContent {
            PrivacyPolicy()
        }
        composeTestRule
            .onNodeWithText("Privacy Policy : Acordai", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun privacyPolicy_conteudoTextoExisteNaArvoreSemantica() {
        composeTestRule.setContent {
            PrivacyPolicy()
        }
        composeTestRule
            .onNode(hasText("Wake Up!", substring = true))
            .assertExists()
    }
}
