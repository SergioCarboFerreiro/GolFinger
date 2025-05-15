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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gousto.kmm.presentation.screen.newRound.uiState.Course
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun SelectCourseScreenComposable(
    onCourseSelected: (Course) -> Unit
) {
    val courses = listOf("Domaio", "Mondariz")
    val types = listOf("18 hoyos", "9 hoyos", "Pitch and Putt")

    var selectedCourse by remember { mutableStateOf<String?>(null) }
    var selectedType by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Seleccionar campo", style = MaterialTheme.typography.headlineSmall)

        courses.forEach { course ->
            OutlinedButton(onClick = { selectedCourse = course }) {
                Text(course)
            }
        }

        Spacer(Modifier.height(8.dp))

        selectedCourse?.let { course ->
            Text("Tipo de campo para $course", style = MaterialTheme.typography.titleMedium)

            types.forEach { type ->
                OutlinedButton(onClick = { selectedType = type }) {
                    Text(type)
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = {
                if (selectedCourse == "Domaio" && selectedType != null) {
                    val course = Course(selectedCourse!!, selectedType!!)
                    onCourseSelected(course) // 👈 aquí haces el callback
                }
            }
        ) {
            Text("Confirmar selección")
        }

        if (selectedCourse == "Mondariz") {
            Text("⚠️ Mondariz no está disponible todavía", color = MaterialTheme.colorScheme.error)
        }
    }
}