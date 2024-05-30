package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CheckCodeRequest(
    val email: String,
    @SerialName("confirm_code")
    val code: String
)