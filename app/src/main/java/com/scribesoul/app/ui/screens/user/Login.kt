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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.scribesoul.R
import com.scribesoul.app.viewModels.AuthViewModel

@Composable
fun Login(navController: NavController,
          viewModel: AuthViewModel
){
    LaunchedEffect(viewModel.isLoggedIn) {
        if (viewModel.isLoggedIn) {
            navController.navigate("home") {
                popUpTo("initial") { inclusive = true } // Clears the login screens from history
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE0EEFF),
                        Color(0xFFE0FEFF),
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
        Column {
            TextField(
                value = viewModel.email,
                onValueChange = {
                    viewModel.email = it
                },
                modifier = Modifier
                    .padding(bottom = 15.dp)
                    .fillMaxWidth(0.85f) // Adjust width to match your layout
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50)),
                placeholder = {
                    Text(text = "Email", color = Color.Gray)
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_email),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent, // Removes the bottom line
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )

// 2. Password Text Field
            TextField(
                value = viewModel.password,
                onValueChange = {
                    viewModel.password = it
                },
                modifier = Modifier
                    .padding(bottom = 30.dp) // Extra padding before the login button
                    .fillMaxWidth(0.85f)
                    .shadow(elevation = 10.dp, shape = RoundedCornerShape(50))
                    .clip(RoundedCornerShape(50)),
                placeholder = {
                    Text(text = "Password", color = Color.Gray)
                },
                leadingIcon = {
                    Image(
                        painter = painterResource(R.drawable.ic_password_custom),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(), // Turns text into dots
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(150.dp))

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
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
                        .clickable {
                            viewModel.login()
                        }
                        .padding( vertical = 13.dp)
                        .width(150.dp)

                ) {
                    Text(
                        text = "LOGIN",
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center
                    )
                }
                if (!viewModel.errorMessage.isNullOrEmpty()) {
                    Text(
                        text = viewModel.errorMessage!!,
                        color = Color.Red,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 4.dp, horizontal = 30.dp)
                            .fillMaxWidth()
                    )
                }
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
        viewModel = viewModel(factory = AuthViewModel.Factory)
    )

}