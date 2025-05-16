package com.gousto.kmm.data.remote.firebase.userRepository

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileModel(
    val id: String = "",
    val name: String = "",
    val handicap: String = ""
)