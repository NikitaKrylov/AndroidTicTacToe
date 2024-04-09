package com.example.tictactoe

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {
    var gameState by mutableStateOf(GameState())
        private set

    var score by mutableStateOf(mutableMapOf('X' to 0, 'O' to 0))
        private set

    private fun changeCurrentPlayer(){
        gameState = gameState.copy(
            currentPlayer =  if (gameState.currentPlayer == 'O') 'X' else 'O'
        )
    }

    private fun getWinner(): Pair<Char, Pair<Offset, Offset>>?  {
//        Return winner sign and position of crossline
        val filed = gameState.field

        return if (filed[0][0] != null && filed[0][0] == filed[0][1] && filed[0][1] == filed[0][2]) { // horizontal 0
            filed[0][0]!! to Pair(Offset(0f, 0f), Offset(2f, 0f))
        } else if (filed[1][0] != null && filed[1][0] == filed[1][1] && filed[1][1] == filed[1][2]) { // horizontal 1
            filed[1][0]!! to Pair(Offset(0f, 1f), Offset(2f, 1f))
        } else if (filed[2][0] != null && filed[2][0] == filed[2][1] && filed[2][1] == filed[2][2]) { // horizontal 2
            filed[2][0]!! to Pair(Offset(0f, 2f), Offset(2f, 2f))
        } else if (filed[0][0] != null && filed[0][0] == filed[1][0] && filed[1][0] == filed[2][0]) { // vertical 0
            filed[0][0]!! to Pair(Offset(0f, 0f), Offset(0f, 2f))
        } else if (filed[0][1] != null && filed[0][1] == filed[1][1] && filed[1][1] == filed[2][1]) { // vertical 1
            filed[0][1]!! to Pair(Offset(1f, 0f), Offset(1f, 2f))
        } else if (filed[0][2] != null && filed[0][2] == filed[1][2] && filed[1][2] == filed[2][2]) { // vertical 2
            filed[0][2]!! to Pair(Offset(2f, 0f), Offset(2f, 2f))
        } else if (filed[0][0] != null && filed[0][0] == filed[1][1] && filed[1][1] == filed[2][2]) { // cross 1
            filed[0][0]!! to Pair(Offset(0f, 0f), Offset(2f, 2f))
        } else if (filed[0][2] != null && filed[0][2] == filed[1][1] && filed[1][1] == filed[2][0]) { // cross 2
            filed[0][2]!! to Pair(Offset(2f, 0f), Offset(0f, 2f))
        } else null
    }

    fun makeMove(x: Int, y: Int){
        if (gameState.field[y][x] != null)
            return

        gameState = gameState.copy(
            field = gameState.field.also {
                it[y][x] = gameState.currentPlayer
            }
        )

        if (gameState.isFieldFull){
            gameState = gameState.copy(
                status = GameStatus.Draw
            )
            score['X'] = score['X']!! + 1
            score['O'] = score['O']!! + 1

        }

        getWinner()?.apply {
            gameState = gameState.copy(
                crossStrokePair = second,
                status = if (first == 'X') GameStatus.WinX else GameStatus.WinO
            )
            score[first] = score[first]!! + 1
        }

        changeCurrentPlayer()
    }

    fun restart(){
        gameState = gameState.copy(
            status = GameStatus.Active,
            field = GameState.emptyField(),
            crossStrokePair = null
        )
    }



}