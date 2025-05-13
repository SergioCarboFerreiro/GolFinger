package com.gousto.kmm.navigation

sealed class Routes(val route: String) {
    data object DashboardScreen : Routes("dashboard")
    data object LoginScreen : Routes("login")
}