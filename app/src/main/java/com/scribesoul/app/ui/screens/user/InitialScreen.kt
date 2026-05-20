package com.scribesoul.app.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.scribesoul.R

@Composable
fun InitialScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF8FBFF),
                        Color(0xFFE9F1FF),
                        Color(0xFFDFEBFF)
                    )
                )
            )
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.cat_onboarding1),
                contentDescription = null,
                modifier = Modifier
                    .width(200.dp),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Express",
                    fontSize = 30.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Yourself",
                    fontSize = 30.sp,

                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFFA774FF), // Purple
                                Color(0xFFFFA8CF),
                                Color(0xFFFFF47A)

                            )
                        )
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Row (horizontalArrangement = Arrangement.spacedBy(10.dp)){
                Text(
                    text = "Find",
                    fontSize = 30.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Peace!",
                    fontSize = 30.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFFF47A),
                                Color(0xFFFFA8CF),
                                Color(0xFFA774FF), // Purple

                            ),
                            // Start at the top-left corner
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            // End at the bottom-right corner (45 degrees)
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )
                    ),
                    textAlign = TextAlign.Center
                )
            }

        }
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50))

                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF82D9D2),
                                    Color(0xFF7CC3E6),
                                    Color(0xFF74A8FF)
                                ),
                                start = Offset(0f, Float.POSITIVE_INFINITY),
                                // End at the bottom-right corner (45 degrees)
                                end = Offset(Float.POSITIVE_INFINITY, 0f)
                            )
                        )
                        .clickable { navController.navigate("login") }
                        .padding( vertical = 13.dp)
                        .width(250.dp)

                ) {
                    Text(
                        text = "LOGIN",
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Column(
                modifier = Modifier
                    .padding(bottom = 30.dp)
            ) {
                Box(
                    modifier = Modifier
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50))
                        .background(
                            Color.White
                        )
                        .clickable { navController.navigate("register") }
                        .padding(vertical = 13.dp)
                        .width(250.dp)
                ) {
                    Text(
                        text = "SIGN UP",
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        color = Color.Black,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewInitialScreen(){
    InitialScreen(navController = NavController(LocalContext.current))
}