package com.chepics.chepics.usecase

import com.chepics.chepics.repository.token.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TokenUseCase {
    fun observeAccessToken(): Flow<String>
}

internal class TokenUseCaseImpl @Inject constructor(
    private val tokenRepository: TokenRepository
) : TokenUseCase {
    override fun observeAccessToken(): Flow<String> {
        return tokenRepository.observeAccessToken()
    }
}