package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class LikeRequest(
    @SerialName("set_id")
    val setId: String,
    @SerialName("comment_id")
    val commentId: String
)

@Keep
@Serializable
data class LikeResponse(
    @SerialName("comment_id")
    val commentId: String,
    @SerialName("has_user_like_comment")
    val isLiked: Boolean,
    @SerialName("comment_like_count")
    val count: Int
)