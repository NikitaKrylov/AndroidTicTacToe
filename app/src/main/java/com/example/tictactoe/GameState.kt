package com.example.tictactoe

import androidx.compose.ui.geometry.Offset


data class GameState(
    val field: Array<Array<Char?>>  = emptyField(),
    val currentPlayer: Char = 'X',
    val isFieldFull: Boolean = false,
    val crossStrokePair: Pair<Offset, Offset>? = null
) {
    companion object {
        fun emptyField(): Array<Array<Char?>> = arrayOf(
            arrayOf(null, null, null),
            arrayOf(null, null, null),
            arrayOf(null, null, null),
        )
    }

    override fun equals(other: Any?): Boolean = this === other


}
