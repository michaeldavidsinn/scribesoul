package com.example.scribesoul.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scribesoul.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {

        }
        composable("therapist") {
            TherapistRecommendationScreen(navController)
        }
        composable("explore") {
            AnonymousChatScreen(navController)
        }
        composable("scribble") {
            ScribbleScreen(navController)
        }
        composable("journal") {

        }
    }
}