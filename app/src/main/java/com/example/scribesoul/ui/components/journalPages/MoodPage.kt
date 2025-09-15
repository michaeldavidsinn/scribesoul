package com.example.scribesoul.ui.components.journalPages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
import java.nio.file.WatchEvent
import java.time.YearMonth


//canvas is required to make the graph, watch youtube vid to make

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MoodPage(list: List<Float> = listOf(4f, 2f, 3f, 1f, 5f,2f)){
    val moodList: List<Float?> = list
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val max = list.max()
    val min = list.min()
    val daysInMonth = 31

    Column(
        modifier = Modifier
            .background(Color(0xFFFFFDB4), shape = RoundedCornerShape(size = 23.dp))
            .height(640.dp)
            .fillMaxWidth(fraction=0.8f)
            .clip(RoundedCornerShape(23.dp))
            .clipToBounds()
            .padding(top = 20.dp),
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(-50.dp)
        ) {
            Text("MOOD", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 50.sp,
                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                    fontWeight = FontWeight(600),
                    color = Color(0XFF2B395B),
                    letterSpacing = 1.sp,
                )
            )
            Text("Tracker", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 96.sp,
                    fontFamily = FontFamily(Font(R.font.palace_script)),
                    fontWeight = FontWeight(500),
                    color = Color.Black,
                    letterSpacing = 1.sp,
                )
            )

        }
        Column(
            modifier = Modifier.offset(y = -15.dp)
        ) {
            CalendarHeader(

                yearMonth = currentMonth,
                onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp, vertical = 0.dp).offset(x = 24.dp),
            horizontalArrangement = Arrangement.End,


        ) {
            Image(painter = painterResource(R.drawable.sad), contentDescription = "", modifier = Modifier.size(15.dp).weight(0.25f))
            Image(painter = painterResource(R.drawable.mid), contentDescription = "", modifier = Modifier.size(15.dp).weight(0.25f))
            Image(painter = painterResource(R.drawable.mid), contentDescription = "", modifier = Modifier.size(15.dp).weight(0.25f))
            Image(painter = painterResource(R.drawable.mid), contentDescription = "", modifier = Modifier.size(15.dp).weight(0.25f))
        }
        Row(
            verticalAlignment = Alignment.Top
        ){
            Column(
                modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "1", fontSize = 12.sp )
                Text(text = "2", fontSize = 12.sp )
                Text(text = "3", fontSize = 12.sp )
                Text(text = "4", fontSize = 12.sp )
                Text(text = "5", fontSize = 12.sp )
                Text(text = "6", fontSize = 12.sp )
                Text(text = "7", fontSize = 12.sp )
                Text(text = "8", fontSize = 12.sp )
                Text(text = "9", fontSize = 12.sp )
                Text(text = "10", fontSize = 12.sp )
                Text(text = "11", fontSize = 12.sp )
                Text(text = "12", fontSize = 12.sp )
                Text(text = "13", fontSize = 12.sp )
                Text(text = "14", fontSize = 12.sp )
                Text(text = "15", fontSize = 12.sp )
                Text(text = "16", fontSize = 12.sp )
                Text(text = "17", fontSize = 12.sp )
                Text(text = "18", fontSize = 12.sp )
                Text(text = "19", fontSize = 12.sp )
                Text(text = "20", fontSize = 12.sp )
                Text(text = "21", fontSize = 12.sp )
                Text(text = "22", fontSize = 12.sp )
                Text(text = "23", fontSize = 12.sp )
                Text(text = "24", fontSize = 12.sp )
                Text(text = "25", fontSize = 12.sp )
                Text(text = "26", fontSize = 12.sp )
                Text(text = "27", fontSize = 12.sp )
                Text(text = "28", fontSize = 12.sp )
                Text(text = "29", fontSize = 12.sp )
                Text(text = "30", fontSize = 12.sp )
                Text(text = "31", fontSize = 12.sp )

                

            }

            Canvas(
                modifier = Modifier
                    .padding(start = 14.dp, end = 36.dp)
                    .padding(vertical = 24.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                val spacingY = size.height / (daysInMonth - 1)
                val graphWidth = size.width


                val fullList = List(daysInMonth) { day ->
                    if (day < moodList.size) moodList[day] else null
                }


                val points = fullList.mapIndexedNotNull { index, value ->
                    value?.let {
                        val percent = getValuePercentageForRange(it, max, min)
                        val x = graphWidth * percent
                        val y = spacingY * index
                        Offset(x, y)
                    }
                }


                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = Color.Black,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 4f
                    )
                }



            }
        }
    }

}

private fun getValuePercentageForRange(value: Float, max: Float, min: Float) =
    (value - min) / (max - min)

@RequiresApi(Build.VERSION_CODES.O)
@Preview
    (showBackground = true, showSystemUi = true)
@Composable
fun MoodPagePreview(){
    MoodPage()
}