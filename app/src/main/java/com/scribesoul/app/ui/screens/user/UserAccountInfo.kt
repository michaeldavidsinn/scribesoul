package com.scribesoul.app.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.scribesoul.app.viewModels.UserProfileViewModel

@Composable
fun UserAccountInfoScreen(
    navController: NavController,
    userViewModel: UserProfileViewModel
) {
    LaunchedEffect(Unit) {
        if (userViewModel.userProfile == null || userViewModel.userEmail.isEmpty()) {
            userViewModel.loadUserProfile()
        }
    }

    val userProfile = userViewModel.userProfile
    val email = userViewModel.userEmail
    val name = userProfile?.name ?: "Loading..."
    // NOTE: If you added birthday to UserDTO, use it here. Otherwise, falling back to age.
    val birthday = "01 January 2000" // Replace with userProfile?.birthday if added to DTO

    val iconGradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF74A8FF), Color(0xFF82D9D2))
    )

    val cardGradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFFF47A), Color(0xFFFFA8CF), Color(0xFFA774FF))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF6F6F6),
                        Color(0xFFFFFFFF),
                        Color(0xFFF6F6F6),
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp, top = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clip(RoundedCornerShape(50))
                        .clickable { navController.popBackStack() }
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Account Info",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    color = Color(0xFF2B395B)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Menu List
            val accountItems = listOf(
                Pair("Name", name),
                Pair("Email", email),
                Pair("Password", "********"),
                Pair("Birthday", birthday)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                accountItems.forEach { (label, value) ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(brush = cardGradientBrush, shape = RoundedCornerShape(50))
                            .padding(1.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White)
                            .clickable {
                                when (label) {
                                    "Name" -> { /* TODO: Navigate to Edit Name */ }
                                    "Email" -> { /* TODO: Navigate to Edit Email */ }
                                    "Password" -> navController.navigate("therapist_change_password")
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = value,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = Color(0xFF2B395B),
                                    fontSize = 16.sp
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Arrow Icon",
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .size(16.dp)
                                    .graphicsLayer(alpha = 0.99f)
                                    .drawWithCache {
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(iconGradientBrush, blendMode = BlendMode.SrcAtop)
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}