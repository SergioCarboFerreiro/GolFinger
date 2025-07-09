package com.gousto.kmm.presentation.screen.newRound

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.ArrowBack
import com.gousto.kmm.util.isIOS
import androidx.navigation.NavHostController
import com.gousto.kmm.navigation.Routes
import com.gousto.kmm.navigation.navModels.CourseNavModel
import com.gousto.kmm.presentation.screen.newRound.events.NewRoundScreenUiEvent
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
    val snackbarHostState = remember { SnackbarHostState() }

    // Recoge el campo que viene desde SelectCourseScreen
    LaunchedEffect(Unit) {
        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.get<String>("selectedCourseJson")
            ?.let { json ->
                val course = Json.decodeFromString<CourseNavModel>(json)
                viewModel.selectCourse(course)
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.remove<String>("selectedCourseJson")
            }
    }
    // Maneja eventos como navegación y errores
    LaunchedEffect(Unit) {
        viewModel.event.collect { event ->
            when (event) {
                is NewRoundScreenUiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }

                is NewRoundScreenUiEvent.RoundCreated -> {
                    navController.navigate("${Routes.RoundScreen.route}/${event.sessionId}")
                }
            }
        }
    }

    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isIOS) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            // Título y selección de campo (fijo arriba)
            Text("🏌️ Crear nueva partida", style = MaterialTheme.typography.headlineSmall)

            OutlinedButton(onClick = onSelectCourseClicked) {
                Text(
                    if (uiState.selectedCourse != null)
                        "Campo: ${uiState.selectedCourse!!.name} - ${uiState.selectedCourse!!.games.firstOrNull()?.type ?: "?"}"
                    else
                        "Debes seleccionar campo"
                )
            }

            Text("Selecciona los jugadores", style = MaterialTheme.typography.titleMedium)

            // Scroll solo para los jugadores
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.players) { player ->
                    val isSelected = uiState.selectedPlayers.contains(player.id)

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.togglePlayerSelection(player.id) },
                        colors = CardDefaults.cardColors(
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(3.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar con inicial
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = player.name.firstOrNull()?.uppercase() ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = player.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (player.handicap.isNotBlank()) {
                                    Text(
                                        text = "HCP: ${player.handicap}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // Botón final (fijo abajo)
            Button(
                onClick = { viewModel.onStartRoundClicked() },
                enabled = uiState.selectedCourse != null && uiState.selectedPlayers.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Comenzar partida")
            }
        }
    }
}