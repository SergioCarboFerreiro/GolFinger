package com.gousto.kmm.presentation.screen.newRound.uiState

import com.gousto.kmm.data.remote.firebase.courseRepository.CourseModel
import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.navigation.navModels.CourseNavModel
import com.gousto.kmm.presentation.screen.newRound.courses.uiState.CourseUiState
import kotlinx.serialization.Serializable

data class NewRoundScreenUiState(
    val courses: List<CourseModel> = emptyList(),
    val type: List<String> = emptyList(),
    val selectedCourse: CourseNavModel? = null,
    val players: List<UserProfileModel> = emptyList(),
    val selectedPlayers: Set<String> = emptySet(),
    val isLoading: Boolean = false
)
