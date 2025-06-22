package com.gousto.kmm.presentation.screen.userStats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gousto.kmm.presentation.screen.userStats.events.UserStatsScreenUiEvent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.math.roundToInt

@OptIn(KoinExperimentalAPI::class)
@Composable
fun UserStatsScreenComposable() {
    val viewModel: UserStatsScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.getUserStatsUiState()
    }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            if (event is UserStatsScreenUiEvent.ShowError) {
                snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            uiState.stats?.let { stats ->
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "📊 Tus estadísticas",
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Divider()

                    StatRow(label = "Rondas jugadas", value = "${stats.totalRounds}")
                    StatRow(
                        label = "Media de golpes",
                        value = stats.averageStrokes.roundToInt().toString()
                    )
                    StatRow(label = "Mejor ronda", value = "${stats.bestRound}")
                    StatRow(label = "Birdies", value = "${stats.totalBirdies}")
                    StatRow(label = "Pars", value = "${stats.totalPars}")
                    StatRow(label = "Bogeys", value = "${stats.totalBogeys}")
                }
            } ?: Text("No hay estadísticas disponibles.")
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}