package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PickSetRequest(
    @SerializedName("topic_id")
    val topicId: String,
    @SerializedName("set_id")
    val setId: String
)