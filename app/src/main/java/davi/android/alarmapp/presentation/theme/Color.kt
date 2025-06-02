package davi.android.alarmapp.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import davi.android.alarmapp.presentation.theme.ColorSuperApp.ColorPrimary
import davi.android.alarmapp.presentation.theme.ColorSuperApp.ColorPrimaryVariant
import davi.android.alarmapp.presentation.theme.ColorSuperApp.colorBlue
import davi.android.alarmapp.presentation.theme.ColorSuperApp.colorPink

object ColorSuperApp {
    val ColorPrimary = Color(0xFF757575)
    val ColorPrimaryVariant = Color(0xFF222121)
    val colorPink = Color(0xFFCF6679)
    val colorBlue = Color(0XFF0086FF)
    val colorIconConfig = Color(0XFF5B7E96)
    val colorIconPainScale = Color(0XFF9146FF)
    val colorIconSync = Color(0XFFE31E71)
    val colorIconEconomic = Color(0XFF0091EA)
    val colorIconDefault = Color(0XFF00C24F)
    val colorIconFTSS = Color(0XFF4563C6)
    val colorIconCustom = Color(0XFF15B76D)
    val colorIconBia = Color(0XFF7646BE)
    val colorIconEcg = Color(0XFFFE7704)
    val colorIconSPO2 = Color(0XFFFF3E26)
    val colorIconRed = Color(0xFFF87868)
    val colorIconPPG = Color(0XFF0072FE)
    val colorIconPPGFiveMin = Color(0XFFE126FF)
    val colorTextChip = Color(0XFF171717)

    val colorBtDefault = Color(0XD9002A4C)
    val colorBtStop = Color(0XFF3B1319)
    val colorBtSecondary = Color(0X1AFFFFFF)
    val colorBtDisable = Color(0XFF333333)
    val colorBtActive = Color(0XFF002442)

    val colorTextBtDefault = Color(0XFF008CFF)
    val colorTextBtDefaultDisable = Color(0x80B2B2B2)
    val colorTextBtStop = Color(0XFFE63232)
    val colorTextBtDisable = Color(0XFF626262)
    val colorTextBtActive = Color(0XFF0072FE)
}

internal val wearColorPalette: Colors = Colors(
    primary = ColorPrimary,
    primaryVariant = ColorPrimaryVariant,
    secondary = colorBlue,
    secondaryVariant = colorBlue,
    error = colorPink,
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onError = Color.Black,
    background = Color.Black
)

