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

        composable("therapist_detail") {
            TherapistDetailScreen(navController)
        }

        composable("therapist_account_info") {
            TherapistAccountInfoScreen(navController)
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

        composable("therapist_privacy_policy") {
            TherapistPrivacyPolicyScreen(navController)
        }

        composable("explore") {
            AnonymousChatScreen(navController)
        }

        composable("join_chat") {
            JoinChatScreen(navController)
        }

        composable("community_group") {
            CommunityGroupScreen(navController)
        }

        composable("scribble") {
            ScribbleScreen(navController)
        }

        composable("add_scribble") {
            AddScribbleScreen(navController)
        }

        composable("journal") {

        }
    }
}