package com.gousto.kmm.presentation.screen.userStats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
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
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(16.dp)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            uiState.stats?.let { stats ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 48.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "📊 Tus estadísticas",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )

                    StatCard("Resumen") {
                        StatRow("Rondas jugadas", "${stats.totalRounds}")
                        StatRow("Media de golpes", stats.averageStrokes.round(1))
                        StatRow("Mejor ronda", "${stats.bestRound}")
                    }

                    StatCard("Puntajes por tipo") {
                        StatRow("Birdies", "${stats.totalBirdies}")
                        StatRow("Pars", "${stats.totalPars}")
                        StatRow("Bogeys", "${stats.totalBogeys}")
                    }

                    if (stats.statsByType.isNotEmpty()) {
                        Text("📂 Por tipo de juego", style = MaterialTheme.typography.titleMedium)
                        stats.statsByType.forEach { (type, detail) ->
                            StatCard(mapGameTypeToLabel(type)) {
                                StatRow("Rondas", "${detail.rounds}")
                                StatRow("Media de golpes", detail.averageStrokes.round(1))
                                StatRow("Mejor ronda", "${detail.bestRound}")
                            }
                        }
                    }
                }
            } ?: Text(
                "No hay estadísticas disponibles.",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun StatCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            content()
        }
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