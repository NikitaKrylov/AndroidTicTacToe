package com.example.tictactoe

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.tictactoe.ui.theme.OnDarkTicTactToePalette
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
                    GameScreen(
                        gameViewModel.gameState,
                        gameViewModel.score,
                        gameViewModel::makeMove,
                        gameViewModel::restart
                    )
                }
            }
        }
    }
}

@Composable
fun GameScreen(
    state: GameState,
    score: Map<Char, Int>,
    onTapGesture: (Int, Int) -> Unit,
    onRestartAction: () -> Unit
) {

    var showEndDialog = remember { mutableStateOf(false) }

    if (state.status != GameStatus.Active){
        showEndDialog.value = true
    }

    if (showEndDialog.value){
        EndDialog(
            onRestartAction = onRestartAction,
            setShowDialog = {showEndDialog.value = it},
            state = state
        )
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .fillMaxSize()
            .background(OnDarkTicTactToePalette.background)
    ) {
        PlayerLabel(
            state,
            rate = score['O']!!,
            player = 'O',
            onShowMenuAction = {showEndDialog.value = it},
            reversed = true
        )

        TicTacToeField(
            state,
            onTapGesture
        )

        PlayerLabel(
            state,
            rate = score['X']!!,
            onShowMenuAction = {showEndDialog.value = it},
            player = 'X'
        )

    }
}


private fun DrawScope.drawO(
    color: Color,
    center: Offset,
    size: Size
){
    drawCircle(
        center = center,
        radius = size.width / 2.5f,
        color = color,
        style = Stroke(width = 25.dp.toPx())
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
            x = center.x - size.width / 3f,
            y = center.y - size.height / 3f
        ),
        end = Offset(
            x = center.x + size.width / 3f,
            y = center.y + size.height / 3f
        ),
        strokeWidth = 25.dp.toPx(),
        cap = StrokeCap.Square
    )

    drawLine(
        color = color,
        start = Offset(
            x = center.x - size.width / 3f,
            y = center.y + size.height / 3f
        ),
        end = Offset(
            x = center.x + size.width / 3f,
            y = center.y - size.height / 3f
        ),
        strokeWidth = 25.dp.toPx(),
        cap = StrokeCap.Square
    )
}

private fun DrawScope.drawCrossLine(
    start: Offset,
    end: Offset,
    color: Color,
    size: Size,
    dimensions: Int = 3
){

    drawLine(
        color = color,
        start = Offset(
            x = size.width / (dimensions * 2).toFloat() * (start.x * 2 + 1) ,
            y = size.height / (dimensions * 2).toFloat() * (start.y * 2f + 1),
        ),
        end = Offset(
            x = size.width / (dimensions * 2).toFloat() * (end.x * 2 + 1) ,
            y = size.height / (dimensions * 2).toFloat() * (end.y * 2f + 1),
        ),
        strokeWidth = 7.dp.toPx(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawField(
    dimensions: Int = 3,
    color: Color = OnDarkTicTactToePalette.playSurfaceDivide,
    padding: Dp = 15.dp
){
    (1 until dimensions).forEach { index ->
        drawLine(
            color = color,
            start = Offset(
                x = size.width * (index / dimensions.toFloat()),
                y = padding.toPx()
            ),
            end = Offset(
                x = size.width * (index / dimensions.toFloat()),
                y = size.height - padding.toPx()
            ),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(
                x = padding.toPx(),
                y = size.width * (index / dimensions.toFloat())
            ),
            end = Offset(
                x = size.width - padding.toPx(),
                y = size.width * (index / dimensions.toFloat())
            ),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}


@Composable
fun EndDialogTitle(
    text: String = "Menu"
) {
    Text(
        text = text, fontSize = 50.sp, color = Color.White
    )
}


@Composable
fun EndDialog(
    setShowDialog: (Boolean) -> Unit,
    state: GameState,
    onRestartAction: () -> Unit
) {
    Dialog(
        onDismissRequest = { setShowDialog(true) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){

            if (state.status == GameStatus.Active){
                Image(
                    modifier = Modifier
                        .width(70.dp)
                        .align(Alignment.TopEnd)
                        .clickable {
                            setShowDialog(false)
                        }
                    ,
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = "close",
                    contentScale = ContentScale.Crop,

                    )
            }



            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (state.status){
                    GameStatus.Active -> {
                        EndDialogTitle("Menu")
                    }
                    GameStatus.Draw -> {
                        EndDialogTitle("Draw")
                    }
                    GameStatus.WinO -> {
                        EndDialogTitle("O Win")
                    }
                    GameStatus.WinX -> {
                        EndDialogTitle("X Win")
                    }
                }

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OnDarkTicTactToePalette.cellBackground
                    ),
                    onClick = {
                    setShowDialog(false)
                    onRestartAction()
                }) {
                    Text(
                        text = "Restart",
                        fontSize = 22.sp,
                        color = Color.White
                    )
                }
            }
        }

    }
}

@Composable
fun PlayerLabel(
    state: GameState,
    name: String = "Player",
    player: Char = 'X',
    onShowMenuAction: (Boolean) -> Unit,
    rate: Int = 0,
    defaultColor: Color = OnDarkTicTactToePalette.cellBackground,
    actionColor: Color = OnDarkTicTactToePalette.actionCellBackground,
    reversed: Boolean = false
) {

    val currentColor = if (state.currentPlayer == player)
        actionColor
    else
        defaultColor

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ){
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = OnDarkTicTactToePalette.cellBackground
            ),
            border = BorderStroke(3.dp, currentColor),
            modifier = Modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(30.dp, 20.dp)
            ) {

                if (reversed){
                    Text(
                        text = rate.toString(),
                        fontSize = 28.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    Text(
                        text = "$name $player",
                        fontSize = 30.sp,
                        color = Color.White
                    )

                } else {
                    Text(
                        text = "$name $player",
                        fontSize = 30.sp,
                        color = Color.White
                    )
                    Text(
                        text = rate.toString(),
                        fontSize = 28.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

            }
        }

        Button(
            onClick = { onShowMenuAction(true) },
            modifier = Modifier
                .align(if (reversed) Alignment.TopStart else Alignment.BottomEnd)
                .padding(10.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = OnDarkTicTactToePalette.playSurface
            ),
            shape = RoundedCornerShape(10.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.baseline_density_medium_24), contentDescription = "menuBtn")
        }
    }


}

@Composable
fun TicTacToeField(
    state: GameState,
    onTapGesture: (Int, Int) -> Unit,
    colorO: Color = OnDarkTicTactToePalette.circle,
    colorX: Color = OnDarkTicTactToePalette.cross,
    dimensions: Int = 3
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = OnDarkTicTactToePalette.playSurface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        ),
        shape = RoundedCornerShape(30.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true) {
                    detectTapGestures { position ->
                        val x = (dimensions * position.x.toInt() / size.width)
                        val y = (dimensions * position.y.toInt() / size.height)
                        Log.i("TicTacToe", "Press x: $x y: $y")

                        onTapGesture(x, y)
                    }
                }
        ) {
            drawField(dimensions)

            val cellSize = Size(size.width / (dimensions + 1).toFloat(), size.height / (dimensions + 1).toFloat())

            state.field.forEachIndexed { x, _ ->
                state.field.forEachIndexed { y, _ ->

                    val cellX = x * 2 + 1
                    val cellY = y * 2 + 1

                    if (state.field[y][x] == 'X'){
                        drawX(
                            colorX,
                            Offset(
                                x = size.width * (cellX / (dimensions * 2).toFloat()),
                                y = size.height * (cellY / (dimensions * 2).toFloat())
                            ),
                            cellSize
                        )
                    } else if (state.field[y][x] == 'O'){
                        drawO(
                            colorO,
                            Offset(
                                x = size.width * (cellX / (dimensions * 2).toFloat()),
                                y = size.height * (cellY / (dimensions * 2).toFloat())
                            ),
                            cellSize
                        )
                    }
                }
            }
            state.crossStrokePair?.apply {
                drawCrossLine(first, second, Color.White, size)

            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    GameScreen(
        GameState(
            field = arrayOf(
                arrayOf(null, 'X', null),
                arrayOf('O', null, null),
                arrayOf(null, null, 'X')
            )
        ),
        mapOf('X' to 12, 'O' to 3),
        {_, _ -> },
        {}
    )
}