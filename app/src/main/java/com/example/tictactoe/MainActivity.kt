package com.example.tictactoe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeField(
                        gameViewModel.gameState,
                        gameViewModel::makeMove
                    )
                }
            }
        }
    }
}


private fun DrawScope.drawO(
    color: Color,
    center: Offset,
    size: Size
){
    drawCircle(
        center = center,
        radius = size.width / 2f,
        color = color,
        style = Stroke(width = 7.dp.toPx())
    )
}

private fun DrawScope.drawX(
    color: Color,
    center: Offset,
    size: Size
){
    drawLine(
        color = color,
        start = Offset(
            x = center.x - size.width / 2f,
            y = center.y - size.height / 2f
        ),
        end = Offset(
            x = center.x + size.width / 2f,
            y = center.y + size.height / 2f
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

    drawLine(
        color = color,
        start = Offset(
            x = center.x - size.width / 2f,
            y = center.y + size.height / 2f
        ),
        end = Offset(
            x = center.x + size.width / 2f,
            y = center.y - size.height / 2f
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawCrossLine(
    start: Offset,
    end: Offset,
    color: Color,
    size: Size
){

    drawLine(
        color = color,
        start = Offset(
            x = size.width / 6f * (start.x * 2 + 1) ,
            y = size.height / 6f * (start.y * 2f + 1),
        ),
        end = Offset(
            x = size.width / 6f * (end.x * 2 + 1) ,
            y = size.height / 6f * (end.y * 2f + 1),
        ),
        strokeWidth = 5.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawField(

){
//    1st vertical line
    drawLine(
        color = Color.Black,
        start = Offset(
            x = size.width * (1 / 3f),
            y = 0f
        ),
        end = Offset(
            x = size.width * (1 / 3f),
            y = size.height
            ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

// 2nd vertical line
    drawLine(
        color = Color.Black,
        start = Offset(
            x = size.width * (2 / 3f),
            y = 0f
        ),
        end = Offset(
            x = size.width * (2 / 3f),
            y = size.height
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

//    1st horizontal line
    drawLine(
        color = Color.Black,
        start = Offset(
            x = 0f,
            y = size.width * (1 / 3f)
        ),
        end = Offset(
            x = size.width,
            y = size.width * (1 / 3f)
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

    //    1st horizontal line
    drawLine(
        color = Color.Black,
        start = Offset(
            x = 0f,
            y = size.width * (2 / 3f)
        ),
        end = Offset(
            x = size.width,
            y = size.width * (2 / 3f)
        ),
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )
}

@Composable
fun TicTacToeField(
    state: GameState,
    onTapGesture: (Int, Int) -> Unit,
    colorO: Color = Color.Red,
    colorX: Color = Color.Green
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectTapGestures { position ->
                        val x = (3 * position.x.toInt() / size.width)
                        val y = (3 * position.y.toInt() / size.height)
                        Log.i("TicTacToe", "Press x: $x y: $y")

                        onTapGesture(x, y)
                    }
                }
        ) {
            drawField()

            val cellSize = Size(size.width / 4f, size.height / 4f)

            state.field.forEachIndexed { x, _ ->
                state.field.forEachIndexed { y, _ ->

                    val cellX = x * 2 + 1
                    val cellY = y * 2 + 1

                    if (state.field[y][x] == 'X'){
                        drawX(
                            colorX,
                            Offset(
                                x = size.width * (cellX / 6f),
                                y = size.height * (cellY / 6f)
                            ),
                            cellSize
                        )
                    } else if (state.field[y][x] == 'O'){
                        drawO(
                            colorO,
                            Offset(
                                x = size.width * (cellX / 6f),
                                y = size.height * (cellY / 6f)
                            ),
                            cellSize
                        )
                    }
                }
            }
            state.crossStrokePair?.apply {
                drawCrossLine(first, second, Color.Gray, size)

            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TicTacToeFieldPreview() {
    TicTacToeField(
        GameState(
            field = arrayOf(
                arrayOf(null, 'X', null),
                arrayOf('O', null, null),
                arrayOf(null, null, 'X')
            )
        ),
        {_, _ -> }
    )
}