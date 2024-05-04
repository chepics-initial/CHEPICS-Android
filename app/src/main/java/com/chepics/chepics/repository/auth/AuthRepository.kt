package com.chepics.chepics.repository.auth

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    suspend fun login(request: LoginRequest): CallResult<Unit>
    suspend fun createCode(email: String): CallResult<String>
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

    override suspend fun createCode(email: String): CallResult<String> {
        return withContext(ioDispatcher) {
            authDataSource.createCode(email)
        }
    }
}