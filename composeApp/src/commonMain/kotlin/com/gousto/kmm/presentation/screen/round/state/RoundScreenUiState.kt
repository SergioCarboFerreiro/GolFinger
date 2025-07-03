package com.gousto.kmm.presentation.screen.round.state

import com.gousto.kmm.data.remote.firebase.courseRepository.CourseModel
import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel

data class RoundScreenUiState(
    val sessionId: String = "",
    val course: CourseModel? = null,
    val players: List<UserProfileModel> = emptyList(),
    val scores: Map<Pair<Int, String>, String> = emptyMap(),
    val isLoading: Boolean = false
)