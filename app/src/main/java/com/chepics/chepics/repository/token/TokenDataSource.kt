package com.chepics.chepics.repository.token

import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    suspend fun storeToken(accessToken: String, refreshToken: String)
    suspend fun removeToken()
    fun observeAccessToken(): Flow<String>
    suspend fun setAccessToken()
}