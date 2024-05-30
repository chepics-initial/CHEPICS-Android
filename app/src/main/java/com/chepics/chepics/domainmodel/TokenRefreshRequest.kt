package com.chepics.chepics.domainmodel

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TokenRefreshRequest(
    val refreshToken: String
)
