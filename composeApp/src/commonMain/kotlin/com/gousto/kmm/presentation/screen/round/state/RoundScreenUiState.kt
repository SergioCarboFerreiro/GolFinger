package com.gousto.kmm.presentation.screen.round.state

import com.gousto.kmm.data.remote.firebase.courseRepository.CourseModel
import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import com.gousto.kmm.location.GeoPoint

data class RoundScreenUiState(
    val sessionId: String = "",
    val course: CourseModel? = null,
    val players: List<UserProfileModel> = emptyList(),
    val scores: Map<Pair<Int, String>, String> = emptyMap(),
    val isLoading: Boolean = false,
    val startLocation: GeoPoint? = null,
    val endLocation: GeoPoint? = null,
    val shotDistanceMeters: Double? = null
)
