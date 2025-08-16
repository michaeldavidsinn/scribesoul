package com.example.scribesoul.ui.components.journalPages

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.scribesoul.ui.screens.ToolMode

//(0XFFFFFEE4)

@Composable
fun PlainPage(currentToolMode: ToolMode){
    val paths = remember { mutableStateListOf<Pair<List<Offset>, ToolMode>>() }
    var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var color: Color by remember { mutableStateOf(Color(0XFF9DF7FF)) }

    Box(
        modifier = Modifier
            .background(color, shape = RoundedCornerShape(size = 23.dp))
            .height(600.dp)
            .fillMaxWidth(fraction=0.8f)

            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()

    ){

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
