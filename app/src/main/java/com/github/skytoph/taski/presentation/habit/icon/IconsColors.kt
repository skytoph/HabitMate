package com.github.skytoph.taski.presentation.habit.icon

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val BlueBright = Color(0xFF4F4FE5)
val BlueGreen = Color(0xFF66A0C0)
val BlueLight = Color(0xFF8CB4CE)
val GreenFresh = Color(0xFF76A8AB)
val GreenLight = Color(0xFFB0D1B9)
val GreenBright = Color(0xFFACC77D)
val GreenSalad = Color(0xFF74A974)
val GreenDark = Color(0xFF577760)
val PinkLight = Color(0xFFF0CCD8)
val Pink = Color(0xFFE188AE)
val Peachy = Color(0xFFE79DA4)
val CoralLight = Color(0xFFE7A290)
val BrownLight = Color(0xFFC2A59E)
val YellowLight = Color(0xFFE7D396)
val YellowBright = Color(0xFFF0B459)
val Orange = Color(0xFFD16B3C)
val Red = Color(0xFFB23052)
val PinkPurple = Color(0xFFC2A9E7)
val GrayLight = Color(0xFFB8B8DE)
val GrayDark = Color(0xFF716D89)
val BlueDark = Color(0xFF424270)
val PurpleDark = Color(0xFF6A4F80)

@Immutable
object IconsColors {
    val allColors: List<Color> = listOf(
        BlueBright,
        BlueGreen,
        BlueLight,
        GreenFresh,
        GreenLight,
        GreenBright,
        GreenSalad,
        GreenDark,
        PinkLight,
        Pink,
        Peachy,
        CoralLight,
        BrownLight,
        YellowLight,
        YellowBright,
        Orange,
        Red,
        PinkPurple,
        GrayLight,
        GrayDark,
        PurpleDark,
        BlueDark
    )

    val Default = allColors.first()
}