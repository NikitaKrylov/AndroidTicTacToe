package com.example.tictactoe

import androidx.compose.ui.geometry.Offset


enum class GameStatus{
    Draw,
    WinX,
    WinO,
    Active
}

data class GameState(
    val field: Array<Array<Char?>> = emptyField(),
    val currentPlayer: Char = 'X',
    val crossStrokePair: Pair<Offset, Offset>? = null,
    val status: GameStatus = GameStatus.Active
) {
    val isFieldFull: Boolean
        get() {
            this.field.forEachIndexed { y, _ ->
                this.field.forEachIndexed { x, _ ->
                    if (this.field[y][x] == null) return false
                }
            }
            return true

        }

    companion object {
        fun emptyField(): Array<Array<Char?>> = arrayOf(
            arrayOf(null, null, null),
            arrayOf(null, null, null),
            arrayOf(null, null, null),
        )
    }

    override fun equals(other: Any?): Boolean = this === other


}
