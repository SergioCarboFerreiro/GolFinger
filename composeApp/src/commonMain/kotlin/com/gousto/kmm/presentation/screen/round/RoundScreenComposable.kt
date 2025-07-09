package com.gousto.kmm.presentation.screen.round

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.ArrowBack
import com.gousto.kmm.util.isIOS
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gousto.kmm.navigation.Routes
import com.gousto.kmm.presentation.screen.round.events.RoundScreenUiEvent
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RoundScreenComposable(
    navController: NavHostController,
    sessionId: String
) {
    val viewModel: RoundScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    var currentHoleIndex by remember { mutableStateOf(0) }
    val holes = uiState.course?.games?.firstOrNull()?.holes.orEmpty()
    val hole = holes.getOrNull(currentHoleIndex)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(sessionId) { viewModel.loadRound(sessionId) }

    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is RoundScreenUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is RoundScreenUiEvent.RoundFinished -> {
                    navController.navigate(Routes.DashboardScreen.route) {
                        popUpTo(Routes.RoundScreen.route) { inclusive = true }
                    }
                }
            }
        }
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (isIOS) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(Modifier.height(8.dp))
        }
        // Información de la ronda
        Column {
            Text("🏌 Seguimiento de ronda", style = MaterialTheme.typography.headlineSmall)
            Text("${uiState.course?.name ?: ""} – Hoyo ${hole?.number ?: "-"} / ${holes.size}")
            Spacer(Modifier.height(8.dp))
        }

        hole?.let { h ->
            Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Hoyo ${h.number}", style = MaterialTheme.typography.titleMedium)
                    Text("Par ${h.par}", style = MaterialTheme.typography.bodyMedium)

                    Spacer(Modifier.height(12.dp))
                    uiState.players.forEach { player ->
                        val key = h.number to player.id
                        val strokes = uiState.scores[key]?.toIntOrNull() ?: 0
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(player.name)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = {
                                        viewModel.updateScore(
                                            h.number,
                                            player.id,
                                            (strokes - 1).coerceAtLeast(0).toString()
                                        )
                                    }
                                ) { Icon(Icons.Default.ArrowDropDown, null) }
                                Text(
                                    "$strokes",
                                    Modifier.width(32.dp),
                                    textAlign = TextAlign.Center
                                )
                                IconButton(
                                    onClick = {
                                        viewModel.updateScore(
                                            h.number,
                                            player.id,
                                            (strokes + 1).toString()
                                        )
                                    }
                                ) { Icon(Icons.Default.KeyboardArrowUp, null) }
                            }
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }

        // Navegación y finalizar
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(
                onClick = { currentHoleIndex = (currentHoleIndex - 1).coerceAtLeast(0) },
                enabled = currentHoleIndex > 0
            ) { Text("Anterior") }

            OutlinedButton(
                onClick = {
                    currentHoleIndex = (currentHoleIndex + 1).coerceAtMost(holes.lastIndex)
                },
                enabled = currentHoleIndex < holes.lastIndex
            ) { Text("Siguiente") }
        }

        Button(
            onClick = { viewModel.finishRound() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar ronda")
        }
    }
}