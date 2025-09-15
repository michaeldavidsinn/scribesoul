package com.example.scribesoul.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.example.scribesoul.ui.screens.HomeScreen
import java.nio.file.WatchEvent

fun splitWords(sentence: String): Array<String>{
    if (' ' !in sentence) {
        return arrayOf("", sentence)
    }


    val lastSpaceIndex = sentence.lastIndexOf(' ')

    val firstPart = sentence.substring(0, lastSpaceIndex)

    val lastWord = sentence.substring(lastSpaceIndex + 1)


    return arrayOf(firstPart, lastWord)

}

@Composable
fun JournalCover(navController: NavController, journalTitle: String, journalDate: String, journalId: String){
    Column(
        modifier = Modifier.clickable{
            navController.navigate("journal")
        },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier =
                Modifier
                    .width(190.dp)
                    .height(250.dp)
                    .align(alignment = Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFA774FF),
                            Color(0xFFFFA8CF),
                            Color(0xFFFFF47A)
                        )
                    )
                ),
            contentAlignment = Alignment.Center

        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(-30.dp)
            ) {

                if(splitWords(journalTitle).get(0) == ""){

                    Text(journalTitle,
                        style = TextStyle(
                            fontSize = 60.sp,
                            fontFamily = FontFamily(Font(R.font.palace_script)),
                            fontWeight = FontWeight(600),
                            color = Color.White,

                            )
                    )

                }else{

                            Text(splitWords(journalTitle).get(0),
                                modifier = Modifier.padding(start = 55.dp),
                                style = TextStyle(
                                    fontSize = 17.sp,
                                    fontFamily = FontFamily(Font(R.font.palanquin_dark_regular)),
                                    fontWeight = FontWeight(600),
                                    color = Color.White,

                                    )
                            )
                            Text(splitWords(journalTitle).get(1),
                                style = TextStyle(
                                    fontSize = 60.sp,
                                    fontFamily = FontFamily(Font(R.font.palace_script)),
                                    fontWeight = FontWeight(600),
                                    color = Color.White,

                                    ))



                }
            }
        }
        Text(text = journalTitle,
            style = TextStyle(
                fontSize = 9.sp,
                fontFamily = FontFamily(Font(R.font.poppins_medium)),
                fontWeight = FontWeight(500),
                color = Color(0XFF2B395B),

                )
            )

        Text(text = journalDate,
                style = TextStyle(
                fontSize = 7.sp,
                fontFamily = FontFamily(Font(R.font.poppins_regular)),
                fontWeight = FontWeight(400),
                color = Color(0XFF2B395B),

                )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun JournalCoverView() {
    // Gunakan dummy NavController untuk preview
    JournalCover(navController = NavController(LocalContext.current), journalTitle = "Rex's Mental Journal", journalDate = "21-11-2024", journalId = "123")
}