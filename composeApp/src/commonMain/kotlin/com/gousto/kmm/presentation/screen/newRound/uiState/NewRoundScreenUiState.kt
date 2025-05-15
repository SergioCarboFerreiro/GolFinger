package com.gousto.kmm.presentation.screen.newRound.uiState

import kotlinx.serialization.Serializable

data class NewRoundScreenUiState(
    val selectedCourse: Course? = null,
    val players: List<String> = listOf("Tú")
)

@Serializable
data class Course(
    val name: String,
    val type: String // "18 hoyos", "9 hoyos", "pitch and putt"
)

