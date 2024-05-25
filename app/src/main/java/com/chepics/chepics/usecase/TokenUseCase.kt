package com.chepics.chepics.usecase

import com.chepics.chepics.repository.token.TokenRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

interface TokenUseCase {
    fun observeAccessToken(): StateFlow<String>
}

internal class TokenUseCaseImpl @Inject constructor(
    private val tokenRepository: TokenRepository
) : TokenUseCase {
    override fun observeAccessToken(): StateFlow<String> {
        return tokenRepository.observeAccessToken()
    }
}