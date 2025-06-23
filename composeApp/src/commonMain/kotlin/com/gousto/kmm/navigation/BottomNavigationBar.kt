package com.gousto.kmm.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem(
            route = Routes.DashboardScreen.route,
            label = "Dashboard",
            icon = Icons.Default.Home
        ),
        BottomNavItem(
            route = Routes.ProfileScreen.route,
            label = "Profile",
            icon = Icons.Default.Face
        ),
        BottomNavItem(
            route = Routes.UserStatsScreen.route,
            label = "User Stats",
            icon = Icons.Default.Person
        )
    )

    val currentDestination = navController
        .currentBackStackEntryAsState()
        .value?.destination?.route

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentDestination == item.route,
                onClick = {
                    if (currentDestination != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(Routes.DashboardScreen.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
}


data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)