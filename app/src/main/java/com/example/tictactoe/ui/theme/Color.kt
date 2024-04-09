package com.example.tictactoe.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class TicTactToePalette(
    val cross: Color,
    val circle: Color,
    val crossLine: Color,

    val playSurface: Color,
    val playSurfaceDivide: Color,
    val cellBackground: Color,
    val actionCellBackground: Color,
    val background: Color,
)

val OnDarkTicTactToePalette = TicTactToePalette(
    cross = Color(0xffea174e),
    circle = Color(0xfff9d235),
    crossLine = Color.White,
    playSurface = Color(0xff6449c2),
    playSurfaceDivide = Color(0xff5d45b5),
    cellBackground = Color(0xff0d1133),
    actionCellBackground = Color(0xff34d93a),
    background = Color(0xff18194b),
)


