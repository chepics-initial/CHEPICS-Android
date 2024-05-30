package com.chepics.chepics.usecase

import com.chepics.chepics.repository.net.RequestHeaderRepository
import com.chepics.chepics.repository.token.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TokenUseCase {
    fun observeAccessToken(): Flow<String>
    suspend fun setAccessToken()
    fun setHeaders(headers: Map<String, String>)
}

internal class TokenUseCaseImpl @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val requestHeaderRepository: RequestHeaderRepository
) : TokenUseCase {

    override fun observeAccessToken(): Flow<String> {
        return tokenRepository.observeAccessToken()
    }

    override suspend fun setAccessToken() {
        tokenRepository.setAccessToken()
    }

    override fun setHeaders(headers: Map<String, String>) {
        requestHeaderRepository.setHeaders(headers)
    }
}