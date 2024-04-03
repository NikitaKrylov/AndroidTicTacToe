package com.example.tictactoe

sealed class GameStatus {
    data class Draw(val message: String = "Draw")
    data class PlayerWin(val message: String = "Player X Win")
}