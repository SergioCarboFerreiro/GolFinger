package com.gousto.kmm.data.remote.firebase.roundRepository

import com.gousto.kmm.data.remote.firebase.courseRepository.CourseModel
import com.gousto.kmm.data.remote.firebase.userRepository.UserProfileModel
import kotlinx.serialization.Serializable

@Serializable
data class RoundModel(
    val sessionId: String,
    val course: CourseModel,
    val players: List<UserProfileModel>
)