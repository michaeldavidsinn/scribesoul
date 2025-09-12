package com.example.scribesoul.ui.components.journalPages

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.vector.PathData
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scribesoul.R
import com.example.scribesoul.ui.screens.ToolMode

//(0XFFFFFEE4)

@Composable
fun CalendarPage(currentToolMode: ToolMode){
    val paths = remember { mutableStateListOf<Pair<List<Offset>, ToolMode>>() }
    var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var color: Color by remember { mutableStateOf(Color(0XFFFFCCE3)) }

    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(size = 23.dp))
            .height(680.dp)
            .fillMaxWidth(fraction=0.9f)

            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()

    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(-50.dp)
            ) {
                Text("November", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 38.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color(0XFF2B395B),
                        letterSpacing = 1.sp,
                    )
                )
                Text("2024", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 96.sp,
                        fontFamily = FontFamily(Font(R.font.palace_script)),
                        fontWeight = FontWeight(500),
                        color = Color.Black,
                        letterSpacing = 1.sp,
                    )
                )
            }

            Image(
                painter = painterResource(R.drawable.calendar),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()

            )

            Text("Goals:",
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = FontFamily(Font(R.font.palace_script)),
                    fontWeight = FontWeight(500),
                    color = Color.Black,
                    letterSpacing = 1.sp,
                ))

        }

        Canvas(modifier = Modifier
            .height(600.dp)
            .fillMaxWidth()
            .pointerInput(currentToolMode) {
                detectDragGestures(
                    onDragStart = { startOffset ->
                        currentPath = listOf(startOffset)
                    },
                    onDrag = { change, _ ->
                        currentPath = currentPath + change.position
                    },
                    onDragEnd = {
                        if (currentPath.isNotEmpty()) {
                            paths.add(currentPath to currentToolMode)
                            currentPath = emptyList()
                        }
                    }
                )
            }) {
            drawIntoCanvas { canvas ->
                canvas.saveLayer(size.toRect(), Paint())
                paths.forEach { (points, toolMode) ->
                    drawPathFromOffsets(offsets = points, mode = toolMode)
                }
                drawPathFromOffsets(offsets = currentPath, mode = currentToolMode)
                canvas.restore()
            }
        }

    }

}

// Fungsi menggambar path dari list Offset, tergantung mode aktif
private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPathFromOffsets(
    offsets: List<Offset>,
    mode: ToolMode
) {
    if (offsets.size < 2) return

    val path = Path().apply {
        moveTo(offsets.first().x, offsets.first().y)
        for (i in 1 until offsets.size) {
            lineTo(offsets[i].x, offsets[i].y)
        }
    }

    drawPath(
        path = path,
        color = if (mode == ToolMode.DRAW) Color.Black else Color.Transparent,
        style = Stroke(width = if (mode == ToolMode.DRAW) 4f else 36f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        ,
        blendMode = if (mode == ToolMode.ERASE) BlendMode.Clear else BlendMode.SrcOver
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarPageView(){
    CalendarPage(currentToolMode = ToolMode.DRAW)
}
