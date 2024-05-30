package com.chepics.chepics.infra.datasource.api.auth

import com.chepics.chepics.domainmodel.AuthResponse
import com.chepics.chepics.domainmodel.CheckCodeRequest
import com.chepics.chepics.domainmodel.CreateCode
import com.chepics.chepics.domainmodel.CreateUserRequest
import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.repository.auth.AuthDataSource
import javax.inject.Inject

class AuthRemoteSource @Inject constructor(private val api: AuthApi) : AuthDataSource {
    override suspend fun login(request: LoginRequest): CallResult<AuthResponse> {
        return safeApiCall { api.login(request) }
    }

    override suspend fun createCode(request: CreateCode): CallResult<String> {
        return safeApiCall { api.createCode(request) }.mapSuccess { it.email }
    }

    override suspend fun checkCode(request: CheckCodeRequest): CallResult<String> {
        return safeApiCall { api.checkCode(request) }.mapSuccess { it.email }
    }

    override suspend fun createUser(request: CreateUserRequest): CallResult<AuthResponse> {
        return safeApiCall { api.createUser(request) }
    }

    override suspend fun refreshToken(request: TokenRefreshRequest): CallResult<AuthResponse> {
        return safeApiCall { api.refreshToken(request) }
    }
}