package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class CheckCodeRequest(
    val email: String,
    @SerializedName("confirm_code")
    val code: String
)