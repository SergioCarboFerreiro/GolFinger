package com.gousto.kmm.navigation

fun shouldShowBottomBar(route: String?): Boolean {
    return route in listOf(
        Routes.DashboardScreen.route,
        Routes.ProfileScreen.route,
    )
}


