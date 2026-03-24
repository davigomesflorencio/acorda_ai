package davi.android.alarmapp.presentation.screens.termsofuse

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
class TermsOfUseTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun termsOfUse_telaRenderizaSemErros() {
        composeTestRule.setContent {
            TermsOfUse()
        }
        composeTestRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun termsOfUse_imagemLogoExibida() {
        composeTestRule.setContent {
            TermsOfUse()
        }
        composeTestRule.onNodeWithContentDescription("Image").assertIsDisplayed()
    }

    @Test
    fun termsOfUse_tituloExibido() {
        composeTestRule.setContent {
            TermsOfUse()
        }
        composeTestRule
            .onNodeWithText("Terms of Use: Acordaí!", substring = true)
            .assertIsDisplayed()
    }

    @Test
    fun termsOfUse_conteudoTextoExisteNaArvoreSemantica() {
        composeTestRule.setContent {
            TermsOfUse()
        }
        composeTestRule
            .onNode(hasText("Wake Up!", substring = true))
            .assertExists()
    }
}
