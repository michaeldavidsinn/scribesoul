package com.example.scribesoul.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.scribesoul.R

@Composable
fun MentalTip(navController: NavController){
    Box(
        modifier =
            Modifier.fillMaxSize().background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0XFFFAFFDB),
                        Color(0XFFFFFDE6),
                        Color(0xFFE6FEF8)
                    )
                )
            )
    ){
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 40.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row{
                Text("Anxiety",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_bold)),
                        fontWeight = FontWeight(600),
                        color = Color(0xFF2B395B),

                        textAlign = TextAlign.Center,
                    )
                )
            }

            Text("Anxiety is your body's natural response to stress. It’s a feeling of fear or apprehension about what’s to come.",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.poppins_regular)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF2B395B),

                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.padding(top = 50.dp)
                )
            Image(
                painter = painterResource(R.drawable.cat_anxiety),
                contentDescription = null
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF74A8FF),
                                Color(0xFF82D9D2)
                            )
                        )
                    )

                        .padding(horizontal = 40.dp, vertical = 10.dp)
                ){
                    Text("BACK",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(400),
                            color = Color.White,

                            textAlign = TextAlign.Center,
                        )
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF74A8FF),
                                    Color(0xFF82D9D2)
                                )
                            )
                        )

                        .padding(horizontal = 40.dp, vertical = 10.dp)
                ){
                    Text("NEXT",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.poppins_bold)),
                            fontWeight = FontWeight(400),
                            color = Color.White,

                            textAlign = TextAlign.Center,
                        )
                        )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MentalTipView() {
    // Gunakan dummy NavController untuk preview
    MentalTip(navController = NavController(LocalContext.current))
}
