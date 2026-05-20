package com.scribesoul.app.ui.screens.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.scribesoul.R
import com.scribesoul.app.viewModels.AuthViewModel
import com.scribesoul.app.viewModels.UserProfileViewModel

@Composable
fun Login(navController: NavController,
          viewModel: AuthViewModel,
          userProfileViewModel: UserProfileViewModel
){
    var passwordVisible by remember { mutableStateOf(false ) }
    LaunchedEffect(viewModel.isLoggedIn) {
        if (viewModel.isLoggedIn) {
            // 2. Check if the profile exists in Firestore
            userProfileViewModel.checkIfUserFinishedOnboarding { hasProfile ->
                if (hasProfile) {
                    // Profile found -> Go to Home
                    navController.navigate("home") {
                        popUpTo("initial") { inclusive = true }
                    }
                } else {
                    // No profile -> Go to Onboarding
                    navController.navigate("user_LetUsKnow") {
                        popUpTo("initial") { inclusive = true }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFE0EEFF), Color(0xFFE0FEFF))
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            Text(
                text = "WELCOME BACK",
                fontSize = 32.sp,
                color = Color(0XFF2B395B),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Do you know that lack of sleep can increase hunger hormones?",
                fontSize = 18.sp,
                color = Color(0XFF2B395B),
                textAlign = TextAlign.Center,
                modifier = Modifier.width(300.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Email Field
            Column(modifier = Modifier.padding(bottom = 15.dp)) {
                TextField(
                    value = viewModel.email,
                    onValueChange = { viewModel.email = it },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50)),
                    placeholder = { Text(text = "Email", color = Color.Gray) },
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.ic_email),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black, // Sets typed text to black
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    )
                )
                // Email Error
                if (viewModel.emailError != null) {
                    Text(
                        text = viewModel.emailError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }

            // Password Field
            Column(modifier = Modifier.padding(bottom = 30.dp)) {
                TextField(
                    value = viewModel.password,
                    onValueChange = { viewModel.password = it },
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50)),
                    placeholder = { Text(text = "Password", color = Color.Gray) },
                    leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.ic_password_custom),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    },
                    // Visibility Toggle Icon
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(imageVector = image, contentDescription = "Toggle password visibility", tint = Color.Gray)
                        }
                    },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedTextColor = Color.Black, // Sets typed text to black
                        unfocusedTextColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    )
                )
                // Password Error
                if (viewModel.passwordError != null) {
                    Text(
                        text = viewModel.passwordError!!,
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))

        // ... Your existing buttons (Login and Register navigation) ...
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF82D9D2), Color(0xFF7CC3E6), Color(0xFF74A8FF)),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )
                    )
                    // 1. Disable the click if it's already loading
                    .clickable(enabled = !viewModel.isLoading) {
                        viewModel.login()
                    }
                    .padding(vertical = 13.dp)
                    .width(150.dp),
                contentAlignment = Alignment.Center // Center the content inside the box
            ) {
                // 2. Show the spinner if loading, otherwise show the text
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp), // Keep it small to fit the button
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "LOGIN",
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Backend/General Errors show here
            if (viewModel.generalError != null) {
                Text(
                    text = viewModel.generalError!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                )
            }
        }
        Column (
            modifier = Modifier.padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text("Don't have an account?", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Column(
                modifier = Modifier
                    .padding(bottom = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                        .clip(RoundedCornerShape(50))
                        .padding(top = 10.dp)
                        .background(Color.White)
                        .clickable {
                            viewModel.reset()
                            navController.navigate("register")
                        }
                        .padding( vertical = 20.dp)
                        .width(250.dp)
                        .align(Alignment.CenterHorizontally)

                ) {
                    Text(
                        text = "CREATE AN ACCOUNT",
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
fun PreviewLoginPage(){
    Login(navController = NavController(LocalContext.current),
        viewModel = viewModel(factory = AuthViewModel.Factory),
        userProfileViewModel = viewModel(factory = UserProfileViewModel.Factory)
    )

}