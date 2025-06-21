package com.example.scribesoul.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.scribesoul.R

enum class ToolMode {
    DRAW, ERASE
}

@Composable
fun DrawScribbleScreen(navController: NavController? = null) {
    val paths = remember { mutableStateListOf<Pair<List<Offset>, ToolMode>>() }
    var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var toolMode by remember { mutableStateOf(ToolMode.DRAW) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(toolMode) {
                detectDragGestures(
                    onDragStart = {
                        currentPath = listOf(it)
                    },
                    onDrag = { change, _ ->
                        currentPath = currentPath + change.position
                    },
                    onDragEnd = {
                        if (currentPath.isNotEmpty()) {
                            paths.add(currentPath to toolMode)
                            currentPath = emptyList()
                        }
                    }
                )
            }
    ) {
        // Area Canvas menggambar
        Canvas(modifier = Modifier.fillMaxSize()) {
            paths.forEach { (pathList, mode) ->
                drawPathFromOffsets(pathList, mode)
            }
            drawPathFromOffsets(currentPath, toolMode)
        }

        // Tool Picker (Pencil dan Eraser)
        Row(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pencil),
                contentDescription = "Pencil",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { toolMode = ToolMode.DRAW }
            )
            Image(
                painter = painterResource(id = R.drawable.eraser),
                contentDescription = "Eraser",
                modifier = Modifier
                    .size(36.dp)
                    .clickable { toolMode = ToolMode.ERASE }
            )
        }

        // Bottom Bar di bagian bawah
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            BottomBarScribble()
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
        color = if (mode == ToolMode.DRAW) Color.Black else Color.White,
        style = Stroke(width = if (mode == ToolMode.DRAW) 4f else 36f, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
}



@Preview(showBackground = true, name = "Scribble Preview")
@Composable
fun DrawScribblePreview() {
    DrawScribbleScreen()
}
