package com.scribesoul.app.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.scribesoul.app.ui.screens.*
import com.scribesoul.app.viewModels.DrawingViewModel
import com.scribesoul.app.viewModels.HomeViewModel
import com.scribesoul.app.viewModels.JournalListViewModel
import com.scribesoul.app.viewModels.JournalViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.scribesoul.app.ui.screens.therapist.ClientDetailScreen
import com.scribesoul.app.viewModels.CommunityViewModel
import com.scribesoul.app.ui.screens.therapist.TherapistHomeScreen
import com.scribesoul.app.ui.screens.therapist.ClientTherapistScreen
import com.scribesoul.app.ui.screens.therapist.TherapistScheduleScreen


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation(
    navController: NavHostController,
    journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory),
    journalListViewModel: JournalListViewModel = viewModel(factory = JournalListViewModel.Factory),
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    drawingViewModel: DrawingViewModel = viewModel(factory = DrawingViewModel.Factory ),
    communityViewModel: CommunityViewModel = viewModel(factory = CommunityViewModel.Factory)

) {

    NavHost(navController = navController, startDestination = "home",
        enterTransition = {
            EnterTransition.None
        },
        exitTransition = {
            ExitTransition.None
        },
        popEnterTransition = {
            EnterTransition.None
        },
        popExitTransition = {
            ExitTransition.None
        }) {
        composable("home") {
            HomeScreen(navController, viewModel =  homeViewModel)
        }

        composable("sos_screen") {
            SOSScreen(navController)
        }

        composable("therapist") {
            TherapistRecommendationScreen(navController)
        }

        composable(
            route = "therapist_detail/{therapistName}",
            arguments = listOf(navArgument("therapistName") { type = NavType.StringType })
        ) { backStackEntry ->
            val therapistName = backStackEntry.arguments?.getString("therapistName") ?: "Unknown"

            TherapistDetailScreen(
                navController = navController,
                therapistName = therapistName
            )
        }

        composable("therapist_account_info") {
            TherapistAccountInfoScreen(navController, homeViewModel)
        }

        composable("therapist_history") {
            TherapistHistoryScreen(navController)
        }

        composable("therapist_customer_service") {
            TherapistCustomerServiceScreen(navController)
        }

        composable("therapist_birthday") {
            TherapistBirthdayScreen(navController)
        }

        composable("therapist_change_password") {
            TherapistChangePasswordScreen(navController)
        }

        composable("therapist_faq") {
            TherapistFAQScreen(navController)
        }

        composable("therapist_pro_and_cons") {
            TherapistProAndConsScreen(navController)
        }

        composable("profile"){
            TherapistProfileScreen(navController, homeViewModel)
        }

        composable("therapist_privacy_policy") {
            TherapistPrivacyPolicyScreen(navController)
        }

        composable("explore") {
            AnonymousChatScreen(navController, communityViewModel, isTherapist = false)
        }

        composable("join_chat") {
            JoinChatScreen(navController, communityViewModel)
        }

        composable("community_group") {
            CommunityGroupScreen(navController, communityViewModel)
        }

        composable("addScribble") {
            AddScribbleScreen(navController)
        }
        composable("scribbleDraw") {
            DrawScribbleScreen(navController)
        }
        composable("journalList") {
            JournalListScreen(navController, journalViewModel = journalViewModel, journalListViewModel = journalListViewModel)
        }
        composable("journal") {
            JournalScreen(navController, journalViewModel = journalViewModel,journalListViewModel = journalListViewModel, drawingViewModel = drawingViewModel)
        }
        composable("anxiety") {
            MentalTip(navController)
        }

        // --- NAVIGASI KHUSUS THERAPIST ---
        composable("home_therapist") {
            TherapistHomeScreen(navController, homeViewModel)
        }

        composable("client_therapist") {
            ClientTherapistScreen(navController, homeViewModel)
        }

        composable(
            route = "client_detail/{clientName}",
            arguments = listOf(navArgument("clientName") { type = NavType.StringType })
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("clientName") ?: "Sarah Gibson"
            ClientDetailScreen(navController, homeViewModel, name)
        }

        composable("explore_therapist") {
            AnonymousChatScreen(navController, communityViewModel, isTherapist = true)
        }

        composable("schedule_therapist") {
            TherapistScheduleScreen(navController, homeViewModel)
        }

    }
}