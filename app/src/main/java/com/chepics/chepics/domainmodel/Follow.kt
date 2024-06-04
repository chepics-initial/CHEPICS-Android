package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class FollowRequest(
    @SerialName("user_id")
    val userId: String
)

@Keep
@Serializable
data class FollowResponse(
    @SerialName("user_id")
    val userId: String,
    val isFollow: Boolean
)
