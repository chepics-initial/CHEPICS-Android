package com.chepics.chepics.repository.token

import com.chepics.chepics.domainmodel.LocalAuthInfo
import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    suspend fun storeToken(accessToken: String, refreshToken: String)
    suspend fun removeToken()
    fun observeAuthInfo(): Flow<LocalAuthInfo>
    suspend fun setInitialAuthInfo()
    suspend fun setLoginStatus(isLoggedIn: Boolean)
    suspend fun getRefreshToken(): String
}