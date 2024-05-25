package com.chepics.chepics.repository.token

import kotlinx.coroutines.flow.StateFlow

interface TokenDataSource {
    suspend fun storeToken(accessToken: String, refreshToken: String)
    suspend fun removeToken()
    fun observeAccessToken(): StateFlow<String>
}