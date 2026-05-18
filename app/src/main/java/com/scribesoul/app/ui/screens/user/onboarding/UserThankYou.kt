package com.scribesoul.app.ui.screens.user.onboarding

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
import com.scribesoul.app.ui.screens.user.InitialScreen

@Composable
fun UserThankYouScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF74A8FF),
                        Color(0xFF7CC3E6),
                        Color(0xFF82D9D2)
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
            Row (horizontalArrangement = Arrangement.spacedBy(10.dp)){
                Text(
                    text = "Thank you for your answers!",
                    fontSize = 14.sp,
                    color = Color(0XFF2B395B),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.Center
                )

            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Enjoy your\r\n\r\njourney with\r\n\r\nScribeSoul",
                    fontSize = 40.sp,
                    color = Color(0XFF2B395B),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                    textAlign = TextAlign.Center
                )
            }


        }
    }
}

@Preview
@Composable
fun PreviewUserThankYouScreen(){
    UserThankYouScreen(navController = NavController(LocalContext.current))
}