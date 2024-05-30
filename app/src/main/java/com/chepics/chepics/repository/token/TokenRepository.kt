package com.chepics.chepics.repository.token

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TokenRepository {
    fun observeAccessToken(): Flow<String>
    suspend fun setAccessToken()
}

internal class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {
    override fun observeAccessToken(): Flow<String> {
        return tokenDataSource.observeAccessToken()
    }

    override suspend fun setAccessToken() {
        tokenDataSource.setAccessToken()
    }
}