package com.chepics.chepics.repository.auth

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    suspend fun login(request: LoginRequest): CallResult<Unit>
}

internal class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): AuthRepository {
    override suspend fun login(request: LoginRequest): CallResult<Unit> {
        return withContext(ioDispatcher) {
            authDataSource.login(request)
        }
    }
}