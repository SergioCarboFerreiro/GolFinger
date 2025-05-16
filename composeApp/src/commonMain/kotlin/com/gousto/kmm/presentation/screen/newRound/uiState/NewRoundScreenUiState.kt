package com.gousto.kmm.presentation.screen.newRound.uiState

import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import kotlinx.serialization.Serializable

data class NewRoundScreenUiState(
    val selectedCourse: Course? = null,
    val players: List<UserProfileModel> = emptyList(),
    val selectedPlayers: Set<String> = emptySet(), // usamos UID como identificador
    val isLoading: Boolean = false
)

@Serializable
data class Course(
    val name: String,
    val type: String // "18 hoyos", "9 hoyos", "pitch and putt"
)

