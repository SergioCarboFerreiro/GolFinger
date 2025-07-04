package com.gousto.kmm.presentation.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SplashScreenComposable(
    onUserInRound: (String) -> Unit,
    onUserLoggedIn: () -> Unit,
    onUserNotLoggedIn: () -> Unit
) {
    val viewModel: SplashScreenViewModel = koinViewModel()
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkLoginAndActiveRound()
    }

    LaunchedEffect(state) {
        if (!state.isLoading) {
            when {
                !state.isInActiveRound && state.sessionId != null -> {
                    onUserInRound(state.sessionId!!)
                }

                state.isLoggedIn -> {
                    onUserLoggedIn()
                }

                else -> {
                    onUserNotLoggedIn()
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}