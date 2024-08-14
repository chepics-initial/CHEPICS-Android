package com.chepics.chepics.repository.token

import com.chepics.chepics.domainmodel.LocalAuthInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TokenRepository {
    fun observeAuthInfo(): Flow<LocalAuthInfo>
    suspend fun setInitialAuthInfo()
}

internal class TokenRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : TokenRepository {
    override fun observeAuthInfo(): Flow<LocalAuthInfo> {
        return tokenDataSource.observeAuthInfo()
    }

    override suspend fun setInitialAuthInfo() {
        tokenDataSource.setInitialAuthInfo()
    }
}