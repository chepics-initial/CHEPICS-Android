package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CreateSetRequest(
    @SerialName("topic_id")
    val topicId: String,
    @SerialName("set_name")
    val setText: String
)

@Keep
@Serializable
data class CreateSetResponse(
    @SerialName("set_id")
    val setId: String
)