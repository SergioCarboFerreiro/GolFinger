package com.gousto.kmm.presentation.screen.round

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.presentation.screen.newRound.uiState.Course
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun RoundScreenComposable(
    sessionId: String,
    course: Course,
    players: List<UserProfileModel>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "⛳ Seguimiento de ronda",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Session ID: $sessionId")
        Text("Campo: ${course.name} - ${course.type}")
        Spacer(modifier = Modifier.height(16.dp))

        Text("Jugadores:", style = MaterialTheme.typography.titleMedium)
        players.forEach { player ->
            Text("• ${player.name} (HCP: ${player.handicap})")
        }

        // Aquí irán los hoyos, golpes y tracking real más adelante
    }
}