package com.gousto.kmm.data.remote.firebase.userRepository

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileModel(
    val name: String = "",
    val handicap: String = ""
)