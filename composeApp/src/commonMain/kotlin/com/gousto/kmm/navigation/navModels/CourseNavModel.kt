package com.gousto.kmm.navigation.navModels

import kotlinx.serialization.Serializable

@Serializable
data class CourseNavModel(
    val name: String,
    val location: String = "",
    val games: List<GameModeNavModel> = emptyList()
)

@Serializable
data class GameModeNavModel(
    val type: String,
    val holes: List<HoleNavModel>
)

@Serializable
data class HoleNavModel(
    val number: Int,
    val par: Int
)