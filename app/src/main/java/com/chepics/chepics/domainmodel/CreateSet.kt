package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CreateSetRequest(
    @SerializedName("topic_id")
    val topicId: String,
    @SerializedName("set_name")
    val setText: String
)

@Keep
@Serializable
data class CreateSetResponse(
    @SerializedName("set_id")
    val setId: String
)