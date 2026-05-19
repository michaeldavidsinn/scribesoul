package com.scribesoul.app.ui.screens.user.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

@Composable
fun UserLetUsKnowScreen(navController: NavController){
    // State to trigger the entry animation
    var textVisible by remember { mutableStateOf(false) }

    // Fire the animation and handle the auto-navigation
    LaunchedEffect(Unit) {
        // 1. Kick off the enter animation
        textVisible = true

        // 2. Wait so the user can read the text (e.g., 2 seconds)
        delay(2000L)

        // 3. Flip the state to trigger your exit = fadeOut() + slideOutVertically()
        textVisible = false

        // 4. Wait for your exit animation to actually finish (matches your 800ms tween)
        delay(800L)

        navController.navigate("user_age") {
            popUpTo("let_us_know") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF74A8FF),
                        Color(0xFF7CC3E6),
                        Color(0xFF82D9D2)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // The animation wrapper
        AnimatedVisibility(
            visible = textVisible,
            enter = fadeIn(animationSpec = tween(800)) +
                    slideInVertically(
                        animationSpec = tween(800),
                        initialOffsetY = { fullHeight -> fullHeight / 2 } // Slides up slightly
                    ),
            exit = fadeOut(animationSpec = tween(800)) + slideOutVertically(
                animationSpec = tween(800)
            )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.padding(bottom = 40.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "LET US KNOW\n\r\n\rABOUT YOU",
                        fontSize = 40.sp,
                        color = Color(0XFF2B395B),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
                        textAlign = TextAlign.Center
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                    Text(
                        text = "we’ll ask a few questions about you",
                        fontSize = 14.sp,
                        color = Color(0XFF2B395B),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewUserLetUsKnowScreen(){
    UserLetUsKnowScreen(navController = NavController(LocalContext.current))
}