package com.example.scribesoul.ui.screens



import HabitCalendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.example.scribesoul.ui.components.JournalCover
import com.example.scribesoul.ui.components.journalPages.CreationPage
import com.example.scribesoul.ui.components.journalPages.PlainPage
import com.example.scribesoul.ui.components.splitWords
import com.example.scribesoul.viewModels.JournalViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scribesoul.ui.components.journalPages.HabitsPage


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JournalScreen(navController: NavController, journalViewModel: JournalViewModel){
    val paths = remember { mutableStateListOf<Pair<List<Offset>, ToolMode>>() }
    var toolMode by remember { mutableStateOf(ToolMode.DRAW) }
    var currentPath by remember { mutableStateOf<List<Offset>>(emptyList()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White,
                        Color(0xFFF4FFFE)
                    )
                )
            )
        ,
        contentAlignment = Alignment.TopCenter
    ){
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

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = -20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(fraction = 0.75f).offset(250.dp)
            ) {
                Row(
                    modifier = Modifier

                        .width(94.dp)
                        .height(59.dp)
                        .background(color = Color(0xFF9DF7FF), shape = RoundedCornerShape(size = 23.dp))
                        .clickable(onClick = {
                            journalViewModel.changeSelectedSection(1)
                        })
                ){

                }
                Row(
                    modifier = Modifier

                        .width(94.dp)
                        .height(59.dp)
                        .background(color = Color(0xFF74A8FF), shape = RoundedCornerShape(size = 23.dp))
                        .clickable(onClick = {
                            journalViewModel.changeSelectedSection(0)
                        })
                ) {

                }
            }

            if(journalViewModel.selectedSectionID == 0){
                CreationPage()
            }else{
                PlainPage(toolMode)
            }




        }


        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {

            BottomBarHome()
        }

    }


}

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


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Journalview() {
    JournalScreen(navController = NavController(LocalContext.current), journalViewModel = viewModel(factory = JournalViewModel.Factory))
}