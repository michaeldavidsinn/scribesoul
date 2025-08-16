package com.example.scribesoul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scribesoul.R
import com.example.scribesoul.ui.components.JournalCover

@Composable
fun JournalListScreen(navController: NavController){
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
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 60.dp)
            ) {
                Text("Bulletjournal",
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF2B395B),

                        )
                    )
                Text("Customize your own journal as you wish",
                    style = TextStyle(
                        fontSize = 10.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF2B395B),
                    )
                    )
            }
            Spacer(modifier = Modifier.height(140.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding( top = 5.dp).align(alignment = Alignment.CenterHorizontally), contentPadding = PaddingValues(start = 100.dp, end = 100.dp)
            ) {
                item{
                    JournalCover(navController = navController, journalTitle = "My Mental Journey", journalDate = "21-11-2024", journalId = "123")
                }
                item{
                    JournalCover(navController = navController, journalTitle = "My Mental Journey", journalDate = "21-11-2024", journalId = "123")
                }
                item{
                    JournalCover(navController = navController, journalTitle = "My Mental Journey", journalDate = "21-11-2024", journalId = "123")
                }
                item{
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
                            Text("+",

                                style = TextStyle(
                                    fontSize = 100.sp,
                                    fontFamily = FontFamily(Font(R.font.poppins_bold)),
                                    fontWeight = FontWeight(600),
                                    color = Color.White,

                                    )
                            )
                        }
                    }
                }

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


@Preview(showBackground = true)
@Composable
fun JournalListview() {
    // Gunakan dummy NavController untuk preview
    JournalListScreen(navController = NavController(LocalContext.current))
}