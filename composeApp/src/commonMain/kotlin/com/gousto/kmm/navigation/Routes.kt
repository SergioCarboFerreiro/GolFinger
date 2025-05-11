package com.gousto.kmm.navigation

sealed class Routes(val route: String) {
    data object Dashboard : Routes("dashboard")
    data object LoginScreen : Routes("login")
}