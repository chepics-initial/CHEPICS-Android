package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PickSetRequest(
    val topicId: String,
    val setId: String
)