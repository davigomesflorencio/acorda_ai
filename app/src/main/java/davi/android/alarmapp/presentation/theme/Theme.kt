package davi.android.alarmapp.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import davi.android.alarmapp.presentation.theme.ColorSuperApp.colorBlue
import davi.android.alarmapp.presentation.theme.ColorSuperApp.colorPink

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AlarmAppTheme(
    content: @Composable () -> Unit
) {
    val darkColorScheme = darkColorScheme(
        primary = Color(0XFF096B68),
        primaryContainer = Color(0XFF129990),
        onPrimary = Color(0xFF66ffc7),
        secondary = Color(0XFFFFFBDE),
        secondaryContainer = colorBlue,
        onSecondaryContainer = Color(0XFFcbc8b9),
        surface = Color.Black,
        onSurface = Color(0XFFDDDDDD),
        error = colorPink,
        onSecondary = Color.Black,
        onError = Color.Black,
        background = Color.Black
    )

    val shapes = Shapes(largeIncreased = RoundedCornerShape(36.0.dp))

    MaterialExpressiveTheme(
        colorScheme = darkColorScheme,
        shapes = shapes,
        typography = TypographyAcordai
    ) {
        content()
    }
}