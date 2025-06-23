package com.gousto.kmm.navigation

sealed class Routes(val route: String) {
    data object DashboardScreen : Routes("dashboard")
    data object LoginScreen : Routes("login")
    data object RegisterScreen : Routes("register")
    data object SplashScreen : Routes("splash")
    data object ProfileScreen : Routes("profile")
    data object SelectCourseScreen : Routes("select_course")
    data object NewRoundScreen : Routes("new_round")
    data object RoundScreen : Routes("round")
    data object UserStatsScreen : Routes("user_stats")
}