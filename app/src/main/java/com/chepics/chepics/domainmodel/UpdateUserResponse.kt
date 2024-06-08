package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class UpdateUserResponse(
    @SerialName("user_id")
    val userId: String
)
