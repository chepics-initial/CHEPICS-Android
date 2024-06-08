package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CommentImage(
    @SerialName("comment_id")
    val commentId: String?,
    @SerialName("seq_no")
    val number: Int,
    @SerialName("image_url")
    val url: String
)
