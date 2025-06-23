package com.gousto.kmm.presentation.screen.userStats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import kotlin.math.pow

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
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 48.dp),
                    Arrangement.spacedBy(20.dp),
                    Alignment.CenterHorizontally // por si hay botón en el futuro
                ) {
                    Text("📊 Tus estadísticas", style = MaterialTheme.typography.headlineMedium)
                    Divider()

                    StatRow("Rondas jugadas", "${stats.totalRounds}")
                    StatRow("Media de golpes", stats.averageStrokes.round(1))
                    StatRow("Mejor ronda", "${stats.bestRound}")
                    StatRow("Birdies", "${stats.totalBirdies}")
                    StatRow("Pars", "${stats.totalPars}")
                    StatRow("Bogeys", "${stats.totalBogeys}")

                    if (stats.statsByType.isNotEmpty()) {
                        Divider()
                        Text("📂 Por tipo de juego", style = MaterialTheme.typography.titleMedium)

                        stats.statsByType.forEach { (type, detail) ->
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "• ${mapGameTypeToLabel(type)}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            StatRow("Rondas", "${detail.rounds}")
                            StatRow("Media de golpes", detail.averageStrokes.round(1))
                            StatRow("Mejor ronda", "${detail.bestRound}")
                        }
                    }
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

// Ejemplo para traducir tipos
fun mapGameTypeToLabel(type: String): String = when (type) {
    "9holes" -> "9 hoyos"
    "standard" -> "18 hoyos"
    "pitch and putt" -> "Pitch & Putt"
    else -> type
}

// Redondeo multiplataforma
fun Double.round(decimals: Int): String {
    val factor = 10.0.pow(decimals)
    return (kotlin.math.round(this * factor) / factor).toString()
}