package com.gousto.kmm.presentation.screen.splashScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gousto.kmm.utils.AuthManager

@Composable
fun SplashScreenComposable(
    onUserLoggedIn: () -> Unit,
    onUserNotLoggedIn: () -> Unit
) {
    LaunchedEffect(Unit) {
        if (AuthManager.isUserLoggedIn()) {
            onUserLoggedIn()
        } else {
            onUserNotLoggedIn()
        }
    }
    //todo relamente necesitamos esto?
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}