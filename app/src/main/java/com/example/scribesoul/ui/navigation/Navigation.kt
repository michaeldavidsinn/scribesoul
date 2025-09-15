package com.example.scribesoul.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.scribesoul.ui.screens.*
import com.example.scribesoul.viewModels.JournalViewModel


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation(
    navController: NavHostController,
    journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory)
) {

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
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
        composable("addScribble") {
            AddScribbleScreen(navController)
        }
        composable("scribbleDraw") {
            DrawScribbleScreen(navController)
        }
        composable("journalList") {
            JournalListScreen(navController)
        }
        composable("journal") {
            JournalScreen(navController, journalViewModel = journalViewModel)
        }
        composable("mental") {
            MentalTip(navController)
        }
    }
}