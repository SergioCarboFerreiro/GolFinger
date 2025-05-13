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
import com.gousto.kmm.presentation.screen.login.LoginScreenViewModel

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
            startDestination = Routes.LoginScreen.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.LoginScreen.route) {
                LoginScreenComposable(
                    onLoginSuccess = {
                        navController.navigate(Routes.DashboardScreen.route) {
                            popUpTo(Routes.LoginScreen.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.DashboardScreen.route) {
                DashboardScreenComposable()
            }

            // Puedes descomentar estas cuando estén listas
//            composable(Routes.StatsScreen.route) { StatsScreenComposable() }
//            composable(Routes.ProfileScreen.route) { ProfileScreenComposable() }
        }
    }
}