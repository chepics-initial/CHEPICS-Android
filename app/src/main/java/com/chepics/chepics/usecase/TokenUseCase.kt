package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.LocalAuthInfo
import com.chepics.chepics.repository.net.RequestHeaderRepository
import com.chepics.chepics.repository.token.TokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TokenUseCase {
    fun observeAuthInfo(): Flow<LocalAuthInfo>
    suspend fun setInitialAuthInfo()
    fun setHeaders(headers: Map<String, String>)
}

internal class TokenUseCaseImpl @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val requestHeaderRepository: RequestHeaderRepository
) : TokenUseCase {

    override fun observeAuthInfo(): Flow<LocalAuthInfo> {
        return tokenRepository.observeAuthInfo()
    }

    override suspend fun setInitialAuthInfo() {
        tokenRepository.setInitialAuthInfo()
    }

    override fun setHeaders(headers: Map<String, String>) {
        requestHeaderRepository.setHeaders(headers)
    }
}