package com.gousto.kmm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gousto.kmm.presentation.screen.login.LoginScreenComposable

@Composable
fun AppRoot() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Routes.LoginScreen.route) {
        composable(Routes.LoginScreen.route) {
            LoginScreenComposable(
                onLoginSuccess = {}
            )
        }
//        composable(Screen.Dashboard.route) {
//            DashboardScreen()
//        }
    }
}