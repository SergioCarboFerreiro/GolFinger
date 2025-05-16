package com.gousto.kmm.presentation.screen.newRound

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gousto.kmm.presentation.screen.newRound.uiState.Course
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun NewRoundScreenComposable(
    navController: NavHostController,
    onSelectCourseClicked: () -> Unit,
    onStartRoundClicked: () -> Unit
) {
    val viewModel: NewRoundScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedCourseJson")
            ?.let { json ->
                val course = Json.decodeFromString<Course>(json)
                viewModel.selectCourse(course)
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<String>("selectedCourseJson")
            }
    }
    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🏌️ Crear nueva partida", style = MaterialTheme.typography.headlineSmall)

            OutlinedButton(onClick = onSelectCourseClicked) {
                Text(
                    if (uiState.selectedCourse != null)
                        "Campo: ${uiState.selectedCourse?.name} - ${uiState.selectedCourse?.type}"
                    else
                        "Seleccionar campo"
                )
            }


            Text("Selecciona los jugadores", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))

            LazyColumn {
                items(uiState.players) { player ->
                    val isSelected = uiState.selectedPlayers.contains(player.id) // o UID
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.togglePlayerSelection(player.id) }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { viewModel.togglePlayerSelection(player.id) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Column {
                            Text(player.name)
                            if (player.handicap.isNotBlank()) {
                                Text("HCP: ${player.handicap}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.weight(1f))

            Button(
                onClick = onStartRoundClicked,
                enabled = uiState.selectedCourse != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comenzar partida")
            }
        }
    }
}