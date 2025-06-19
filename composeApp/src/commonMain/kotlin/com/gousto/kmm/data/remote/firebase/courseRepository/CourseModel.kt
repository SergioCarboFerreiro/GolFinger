package com.gousto.kmm.data.remote.firebase.courseRepository

import kotlinx.serialization.Serializable

@Serializable
data class CourseModel(
    val name: String,
    val location: String = "", // valor por defecto
    val games: List<GameModeModel> = emptyList() // valor por defe
)

@Serializable
data class GameModeModel(
    val type: String,       // "standard", "pitch_and_putt"
    val holes: List<HoleModel>
)

@Serializable
data class HoleModel(
    val number: Int,
    val par: Int
)