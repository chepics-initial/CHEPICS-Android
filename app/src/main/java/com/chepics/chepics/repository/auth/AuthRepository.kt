package com.chepics.chepics.repository.auth

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.CheckCodeRequest
import com.chepics.chepics.domainmodel.CreateCode
import com.chepics.chepics.domainmodel.CreateUserRequest
import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.token.TokenDataSource
import com.chepics.chepics.repository.user.UserStoreDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface AuthRepository {
    suspend fun login(request: LoginRequest): CallResult<Unit>
    suspend fun createCode(request: CreateCode): CallResult<String>
    suspend fun checkCode(request: CheckCodeRequest): CallResult<Unit>
    suspend fun createUser(password: String): CallResult<Unit>
    suspend fun logout()
    suspend fun skip()
}

internal class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: AuthDataSource,
    private val userStoreDataSource: UserStoreDataSource,
    private val tokenDataSource: TokenDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepository {
    private var email = ""
    private var accessToken = ""
    override suspend fun login(request: LoginRequest): CallResult<Unit> {
        val result = withContext(ioDispatcher) {
            authDataSource.login(request)
        }

        return when (result) {
            is CallResult.Success -> {
                userStoreDataSource.storeUserId(result.data.userId)
                tokenDataSource.storeToken(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken
                )
                CallResult.Success(Unit)
            }

            is CallResult.Error -> {
                return result
            }
        }
    }

    override suspend fun createCode(request: CreateCode): CallResult<String> {
        return withContext(ioDispatcher) {
            authDataSource.createCode(request)
        }
    }

    override suspend fun checkCode(request: CheckCodeRequest): CallResult<Unit> {
        val result = withContext(ioDispatcher) {
            authDataSource.checkCode(request)
        }
        return when (result) {
            is CallResult.Success -> {
                email = request.email
                CallResult.Success(Unit)
            }

            is CallResult.Error -> result
        }
    }

    override suspend fun createUser(password: String): CallResult<Unit> {
        val result = withContext(ioDispatcher) {
            authDataSource.createUser(CreateUserRequest(email = email, password = password))
        }
        return when (result) {
            is CallResult.Success -> {
                userStoreDataSource.storeUserId(result.data.userId)
                tokenDataSource.storeRefreshToken(result.data.refreshToken)
                accessToken = result.data.accessToken
                CallResult.Success(Unit)
            }

            is CallResult.Error -> result
        }
    }

    override suspend fun logout() {
        tokenDataSource.removeToken()
    }

    override suspend fun skip() {
        tokenDataSource.storeAccessToken(accessToken)
    }
}