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
import com.scribesoul.app.ui.screens.therapist.TherapistAccountInfoScreen
import com.scribesoul.app.ui.screens.therapist.TherapistBirthdayScreen
import com.scribesoul.app.ui.screens.therapist.TherapistChangePasswordScreen
import com.scribesoul.app.ui.screens.therapist.TherapistHistoryScreen
import com.scribesoul.app.ui.screens.therapist.TherapistPrivacyPolicyScreen
import com.scribesoul.app.ui.screens.therapist.TherapistProAndConsScreen
import com.scribesoul.app.ui.screens.therapist.TherapistProfileScreen
import com.scribesoul.app.ui.screens.therapist.TherapistScheduleScreen
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistDescriptionInfo
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistExperienceInfo
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistGenderScreen
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistLicenseInfo
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistPersonalInfo
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistProfessionalInfo
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistQualificationInfo
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistSpecializationScreen
import com.scribesoul.app.ui.screens.therapist.onboarding.TherapistTherapyApproachScreen
import com.scribesoul.app.ui.screens.user.AddScribbleScreen
import com.scribesoul.app.ui.screens.user.AnonymousChatScreen
import com.scribesoul.app.ui.screens.user.CommunityGroupScreen
import com.scribesoul.app.ui.screens.user.DrawScribbleScreen
import com.scribesoul.app.ui.screens.user.HomeScreen
import com.scribesoul.app.ui.screens.user.InitialScreen
import com.scribesoul.app.ui.screens.user.JoinChatScreen
import com.scribesoul.app.ui.screens.user.JournalListScreen
import com.scribesoul.app.ui.screens.user.JournalScreen
import com.scribesoul.app.ui.screens.user.Login
import com.scribesoul.app.ui.screens.user.MentalTip
import com.scribesoul.app.ui.screens.user.Register
import com.scribesoul.app.ui.screens.user.SOSScreen
import com.scribesoul.app.ui.screens.user.TherapistCustomerServiceScreen
import com.scribesoul.app.ui.screens.user.TherapistDetailScreen
import com.scribesoul.app.ui.screens.user.TherapistFAQScreen
import com.scribesoul.app.ui.screens.user.TherapistRecommendationScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserAgeScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserChallengesScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserFeelingScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserGenderScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserImportanceScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserLetUsKnowScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserMainGoalScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserMotivationScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserOftenEmotionalSupportScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserOftenFeelingScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserOftenMoodSwingsScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserProblemsScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserSeekingHelpDurationScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserSoughtMentalHealthScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserStartFeelingScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserSupportKindScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserThankYouScreen
import com.scribesoul.app.ui.screens.user.onboarding.UserTypeOfSupportScreen
import com.scribesoul.app.viewModels.AuthViewModel
import com.scribesoul.app.viewModels.PostViewModel
import com.scribesoul.app.viewModels.TherapistHomeViewModel
import com.scribesoul.app.viewModels.TherapistOnboardingViewModel
import com.scribesoul.app.viewModels.UserOnboardingViewModel


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation(
    navController: NavHostController,
    journalViewModel: JournalViewModel = viewModel(factory = JournalViewModel.Factory),
    journalListViewModel: JournalListViewModel = viewModel(factory = JournalListViewModel.Factory),
    homeViewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory),
    therapistHomeViewModel: TherapistHomeViewModel = viewModel(factory = TherapistHomeViewModel.Factory),
    drawingViewModel: DrawingViewModel = viewModel(factory = DrawingViewModel.Factory ),
    communityViewModel: CommunityViewModel = viewModel(factory = CommunityViewModel.Factory),
    postViewModel: PostViewModel = viewModel(factory = PostViewModel.Factory),
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    userOnboardingViewModel: UserOnboardingViewModel = viewModel(factory = UserOnboardingViewModel.Factory)

) {
    val startScreen = if (authViewModel.isLoggedIn) "home" else "initial"
    val onboardingViewModel: TherapistOnboardingViewModel = viewModel(
        factory = TherapistOnboardingViewModel.Factory
    )

    NavHost(navController = navController, startDestination = startScreen,
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

        composable("initial") {
            InitialScreen(navController)
        }

        composable("login") {
            Login(navController, viewModel = authViewModel)
        }

        composable("register") {
            Register(navController, viewModel = authViewModel)
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
            TherapistAccountInfoScreen(navController, therapistHomeViewModel)
        }

        composable("therapist_history") {
            TherapistHistoryScreen(navController, therapistHomeViewModel)
        }

        composable("therapist_customer_service") {
            TherapistCustomerServiceScreen(navController)
        }

        composable("therapist_birthday") {
            TherapistBirthdayScreen(navController, therapistHomeViewModel)
        }

        composable("therapist_change_password") {
            TherapistChangePasswordScreen(navController, authViewModel)
        }

        composable("therapist_faq") {
            TherapistFAQScreen(navController)
        }

        composable("therapist_pro_and_cons") {
            TherapistProAndConsScreen(navController)
        }

        composable("profile") {
            TherapistProfileScreen(navController, therapistHomeViewModel, authViewModel)
        }

        composable("therapist_privacy_policy") {
            TherapistPrivacyPolicyScreen(navController)
        }

        composable("explore") {
            AnonymousChatScreen(
                navController = navController,
                postViewModel = postViewModel, // Tambahkan parameter ini
                communityViewModel = communityViewModel,
                isTherapist = false
            )
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

        composable("therapist_personal_info") {
            TherapistPersonalInfo(navController, onboardingViewModel)
        }

        composable("therapist_gender") {
            TherapistGenderScreen(navController, onboardingViewModel)
        }

        composable("therapist_professional_info") {
            TherapistProfessionalInfo(navController, onboardingViewModel)
        }

        composable("therapist_specialization") {
            TherapistSpecializationScreen(navController, onboardingViewModel)
        }

        composable("therapist_description") {
            TherapistDescriptionInfo(navController, onboardingViewModel)
        }

        composable("therapist_qualification") {
            TherapistQualificationInfo(navController, onboardingViewModel)
        }

        composable("therapist_experience") {
            TherapistExperienceInfo(navController, onboardingViewModel)
        }

        composable("therapist_license") {
            TherapistLicenseInfo(navController, onboardingViewModel)
        }

        composable("therapist_approaches") {
            TherapistTherapyApproachScreen(navController, onboardingViewModel)
        }


        // Onboarding user
        composable("user_LetUsKnow"){
            UserLetUsKnowScreen(navController)
        }

        composable("user_age") {
            UserAgeScreen(navController, userOnboardingViewModel)
        }

        composable("user_gender") {
            UserGenderScreen(navController, userOnboardingViewModel)
        }

        composable("user_problems") {
            UserProblemsScreen(navController, userOnboardingViewModel)
        }

        composable("user_goals") {
            UserMainGoalScreen(navController, userOnboardingViewModel)
        }

        composable("user_feeling") {
            UserFeelingScreen(navController, userOnboardingViewModel)
        }

        composable("user_start_feeling") {
            UserStartFeelingScreen(navController, userOnboardingViewModel)
        }

        composable("user_often_feeling") {
            UserOftenFeelingScreen(navController, userOnboardingViewModel)
        }

        composable("user_often_mood_swing") {
            UserOftenMoodSwingsScreen(navController, userOnboardingViewModel)
        }

        composable("user_often_emotional_support") {
            UserOftenEmotionalSupportScreen(navController, userOnboardingViewModel)
        }

        composable("user_type_of_support") {
            UserTypeOfSupportScreen(navController, userOnboardingViewModel)
        }

        composable("user_challenges") {
            UserChallengesScreen(navController, userOnboardingViewModel)
        }

        composable("user_motivation") {
            UserMotivationScreen(navController, userOnboardingViewModel)
        }

        composable("user_sought_mental_health"){
            UserSoughtMentalHealthScreen(navController, userOnboardingViewModel)
        }

        composable("user_support_kind") {
            UserSupportKindScreen(navController, userOnboardingViewModel)
        }

        composable("user_seeking_help_duration"){
            UserSeekingHelpDurationScreen(navController, userOnboardingViewModel)
        }

        composable("user_importance"){
            UserImportanceScreen(navController, userOnboardingViewModel)
        }

        composable("user_thank_you") {
            UserThankYouScreen(navController)
        }

        // --- NAVIGASI KHUSUS THERAPIST ---
        composable("home_therapist") {
            TherapistHomeScreen(navController, therapistHomeViewModel)
        }

        composable("client_therapist") {
            ClientTherapistScreen(navController, therapistHomeViewModel)
        }

        composable(
            route = "client_detail/{clientId}",
            arguments = listOf(navArgument("clientId") { type = NavType.StringType })
        ) { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId") ?: ""
            ClientDetailScreen(navController, therapistHomeViewModel, clientId)
        }

        composable("explore_therapist") {
            AnonymousChatScreen(
                navController = navController,
                postViewModel = postViewModel, // Tambahkan parameter ini
                communityViewModel = communityViewModel,
                isTherapist = true
            )
        }

        composable("schedule_therapist") {
            TherapistScheduleScreen(navController, therapistHomeViewModel)
        }
    }
}