package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CreateCommentResponse(
    @SerialName("comment_id")
    val commentId: String
)
