package com.scribesoul.app.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.scribesoul.R
import com.scribesoul.app.viewModels.AuthViewModel
import com.scribesoul.app.viewModels.UserProfileViewModel

@Composable
fun UserProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    userViewModel: UserProfileViewModel
) {
    LaunchedEffect(Unit) {
        userViewModel.loadUserProfile()
    }

    val userProfile = userViewModel.userProfile

    val gradientBrushs = Brush.horizontalGradient(
        colors = listOf(Color(0xFF74A8FF), Color(0xFF82D9D2))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFFF6F6F6), Color(0xFFFFFFFF), Color(0xFFF6F6F6))
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
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.align(Alignment.CenterStart).clip(RoundedCornerShape(50))
                        .clickable { navController.popBackStack() }.padding(8.dp)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
                Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, fontSize = 22.sp),
                    color = Color(0xFF2B395B)
                )
            }

            // Profile Picture
            Box(
                modifier = Modifier.size(160.dp)
                    .border(2.dp, Brush.linearGradient(listOf(Color(0xFF74A8FF), Color(0xFF82D9D2))), CircleShape)
                    .clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.cat2),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name & Age Display
            Text(
                text = userProfile?.name ?: "Loading Name...",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B395B),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Age: ${userProfile?.age ?: "Loading..."}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Menu List
            val chipTexts = listOf("Account Info")
            val gradientBrush = Brush.horizontalGradient(
                colors = listOf(Color(0xFFFFF47A), Color(0xFFFFA8CF), Color(0xFFA774FF))
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                chipTexts.forEach { text ->
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .background(brush = gradientBrush, shape = RoundedCornerShape(50)).padding(1.dp)
                            .clip(RoundedCornerShape(50)).background(Color.White)
                            .clickable {
                                when (text) {
                                    "Account Info" -> navController.navigate("user_account_info")
                                    "Customer Service" -> navController.navigate("user_customer_service")
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = Color(0xFF2B395B)
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = "Arrow",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(16.dp).graphicsLayer(alpha = 0.99f).drawWithCache {
                                    onDrawWithContent {
                                        drawContent()
                                        drawRect(gradientBrushs, blendMode = BlendMode.SrcAtop)
                                    }
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(120.dp))
        }

        // Logout Button
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 45.dp).clip(RoundedCornerShape(50))
                .background(Brush.horizontalGradient(listOf(Color(0xFF82D9D2), Color(0xFF7CC3E6), Color(0xFF74A8FF))))
                .clickable {
                    authViewModel.logout()
                    navController.navigate("initial") { popUpTo(0) }
                }
        ) {
            Text(
                text = "LOG OUT",
                modifier = Modifier.padding(horizontal = 50.dp, vertical = 16.dp),
                color = Color.White,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}