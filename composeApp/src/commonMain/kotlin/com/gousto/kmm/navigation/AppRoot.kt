package com.gousto.kmm.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.gousto.kmm.presentation.screen.dashboard.DashboardScreenComposable
import com.gousto.kmm.presentation.screen.login.LoginScreenComposable
import com.gousto.kmm.presentation.screen.newRound.NewRoundScreenComposable
import com.gousto.kmm.presentation.screen.newRound.courses.SelectCourseScreenComposable
import com.gousto.kmm.presentation.screen.profile.ProfileScreenComposable
import com.gousto.kmm.presentation.screen.register.RegisterScreenComposable
import com.gousto.kmm.presentation.screen.round.RoundScreenComposable
import com.gousto.kmm.presentation.screen.splash.SplashScreenComposable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(currentRoute)) {
                BottomNavigationBar(navController)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SplashScreen.route,
            modifier = Modifier.padding(padding)
        ) {

            composable(Routes.SplashScreen.route) {
                SplashScreenComposable(
                    onUserInRound = { sessionId ->
                        navController.navigate("${Routes.RoundScreen.route}/$sessionId") {
                            popUpTo(Routes.SplashScreen.route) { inclusive = true }
                        }
                    },
                    onUserLoggedIn = {
                        navController.navigate(Routes.DashboardScreen.route) {
                            popUpTo(Routes.SplashScreen.route) { inclusive = true }
                        }
                    },
                    onUserNotLoggedIn = {
                        navController.navigate(Routes.LoginScreen.route) {
                            popUpTo(Routes.SplashScreen.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.LoginScreen.route) {
                LoginScreenComposable(
                    onLoginSuccess = {
                        navController.navigate(Routes.DashboardScreen.route) {
                            popUpTo(Routes.LoginScreen.route) { inclusive = true }
                        }
                    },
                    onRegisterClicked = {
                        navController.navigate(Routes.RegisterScreen.route)
                    },
                    onLoginSuccessAndGameStarted = { sessionId ->
                        navController.navigate("${Routes.RoundScreen.route}/$sessionId") {
                            popUpTo(Routes.LoginScreen.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.DashboardScreen.route) {
                DashboardScreenComposable(
                    onNewRoundClick = {
                        navController.navigate(Routes.NewRoundScreen.route)
                    }
                )
            }

            composable(Routes.RegisterScreen.route) {
                RegisterScreenComposable()
            }

            composable(Routes.ProfileScreen.route) {
                ProfileScreenComposable()
            }

            composable(Routes.NewRoundScreen.route) {
                NewRoundScreenComposable(
                    navController = navController,
                    onSelectCourseClicked = {
                        navController.navigate(Routes.SelectCourseScreen.route)
                    },
                    onStartRoundClicked = {
                        navController.navigate(Routes.RoundScreen.route)
                    }
                )
            }

            composable("${Routes.RoundScreen.route}/{sessionId}") { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""

                RoundScreenComposable(
                    sessionId = sessionId
                )
            }

            composable(Routes.SelectCourseScreen.route) {
                SelectCourseScreenComposable(
                    onCourseSelected = { selectedCourse ->
                        val json = Json.encodeToString(selectedCourse)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedCourseJson", json)
                        navController.popBackStack()
                    },
                )
            }

        }
    }
}