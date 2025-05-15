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
import com.gousto.kmm.presentation.screen.profile.ProfileScreenComposable
import com.gousto.kmm.presentation.screen.register.RegisterScreenComposable
import com.gousto.kmm.presentation.screen.splashScreen.SplashScreenComposable

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
            // 👇 Splash Screen: detecta si hay sesión activa
            composable(Routes.SplashScreen.route) {
                SplashScreenComposable(
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
                    }
                )
            }

            composable(Routes.DashboardScreen.route) {
                DashboardScreenComposable()
            }

            composable(Routes.RegisterScreen.route) {
                RegisterScreenComposable(
                    onRegisterSuccess = {
                        navController.navigate(Routes.DashboardScreen.route) {
                            popUpTo(Routes.LoginScreen.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.ProfileScreen.route) {
                ProfileScreenComposable()
            }

        }
    }
}