package com.gousto.kmm.presentation.screen.newRound.courses

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowBack
import com.gousto.kmm.util.isIOS
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gousto.kmm.navigation.navModels.CourseNavModel
import com.gousto.kmm.navigation.navModels.GameModeNavModel
import com.gousto.kmm.navigation.navModels.HoleNavModel
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.CourseUiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SelectCourseScreenComposable(
    onCourseSelected: (CourseNavModel) -> Unit,
    onBack: () -> Unit
) {
    var selectedCourse by remember { mutableStateOf<CourseNavModel?>(null) }
    var selectedGameType by remember { mutableStateOf<GameModeNavModel?>(null) }

    val viewModel: SelectCourseScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (isIOS) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
        Text("Seleccionar campo", style = MaterialTheme.typography.headlineSmall)

        uiState.courses.forEach { course ->
            val isSelected = selectedCourse?.name == course.name
            SelectableCardComposableCard(
                text = course.name,
                isSelected = isSelected,
                onClick = { selectedCourse = course.toNavModel() }
            )
        }

        selectedCourse?.let { course ->
            Spacer(Modifier.height(16.dp))
            Text("Tipo de juego en ${course.name}", style = MaterialTheme.typography.titleMedium)

            course.games.forEach { gameMode ->
                val isGameSelected = selectedGameType?.type == gameMode.type
                SelectableCardComposableCard(
                    text = mapGameTypeToLabel(gameMode.type),
                    isSelected = isGameSelected,
                    onClick = { selectedGameType = gameMode }
                )
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                selectedCourse?.let { course ->
                    selectedGameType?.let { game ->
                        val filtered = course.copy(games = listOf(game))
                        onCourseSelected(filtered)
                    }
                }
            },
            enabled = selectedCourse != null && selectedGameType != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Confirmar selección")
        }

        if (selectedCourse?.name == "Mondariz") {
            Text(
                "⚠️ Mondariz no está disponible todavía",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

fun mapGameTypeToLabel(type: String): String = when (type) {
    "standard" -> "18 Hoyos"
    "9holes" -> "9 Hoyos"
    "pitch_and_putt" -> "Pitch and Putt (16)"
    else -> type
}

fun CourseUiState.toNavModel(): CourseNavModel {
    return CourseNavModel(
        name = name,
        location = location,
        games = games.map { gameMode ->
            GameModeNavModel(
                type = gameMode.type,
                holes = gameMode.holes.map { hole ->
                    HoleNavModel(
                        number = hole.number,
                        par = hole.par
                    )
                }
            )
        }
    )
}