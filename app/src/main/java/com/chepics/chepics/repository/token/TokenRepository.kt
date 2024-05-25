package com.chepics.chepics.repository.token

import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface TokenRepository {
    fun observeAccessToken(): StateFlow<String>
}

internal class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {
    override fun observeAccessToken(): StateFlow<String> {
        return tokenDataSource.observeAccessToken()
    }
}