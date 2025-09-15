package com.example.scribesoul.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun TherapistChangePasswordScreen(navController: NavController) {

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Validasi password
    val hasUppercase = newPassword.any { it.isUpperCase() }
    val hasLowercase = newPassword.any { it.isLowerCase() }
    val hasNumber = newPassword.any { it.isDigit() }
    val hasSpecial = newPassword.any { !it.isLetterOrDigit() }

    val gradientBorder = Brush.horizontalGradient(
        colors = listOf(Color(0xFFFFF47A), Color(0xFFFFA8CF), Color(0xFFA774FF))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDE6))
            .padding(20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }

                Text(
                    text = "Change Password",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    ),
                    color = Color(0xFF2B395B),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            GradientTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New password"
            )

            Spacer(modifier = Modifier.height(16.dp))

// Confirm Password Input
            GradientTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Re-type new password"
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Password Requirements
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                RequirementItem("Uppercase letter", hasUppercase)
                RequirementItem("Lowercase letter", hasLowercase)
                RequirementItem("Number", hasNumber)
                RequirementItem("Special character", hasSpecial)
            }
        }

        // Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(Color(0xFF82D9D2), Color(0xFF7CC3E6), Color(0xFF74A8FF))
                    ),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 50.dp, vertical = 14.dp)
        ) {
            Text(
                text = "CHANGE PASSWORD",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
fun RequirementItem(text: String, satisfied: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .background(
                    if (satisfied) Color(0xFFADD8FF) else Color.Transparent,
                    CircleShape
                )
                .border(1.dp, Color(0xFF2B395B), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (satisfied) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF2B395B)
        )
    }
}

// Gradient untuk border
val gradientBrush = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFFFF47A),
        Color(0xFFFFA8CF),
        Color(0xFFA774FF)
    )
)

@Composable
fun GradientTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = gradientBrush, shape = RoundedCornerShape(30.dp)) // border luar
            .padding(1.dp) // tebal border
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(30.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(30.dp)),
            visualTransformation = PasswordVisualTransformation(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold // teks input bold
            )
        )
    }
}



@Preview(showBackground = true)
@Composable
fun TherapistChangePasswordPreview() {
    val context = LocalContext.current
    val navController = remember { NavController(context) }

    Surface(modifier = Modifier.fillMaxSize()) {
        TherapistChangePasswordScreen(navController = navController)
    }
}