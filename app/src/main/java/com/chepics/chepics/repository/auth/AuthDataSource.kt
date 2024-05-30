package com.chepics.chepics.repository.auth

import com.chepics.chepics.domainmodel.AuthResponse
import com.chepics.chepics.domainmodel.CheckCodeRequest
import com.chepics.chepics.domainmodel.CreateCode
import com.chepics.chepics.domainmodel.CreateUserRequest
import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import com.chepics.chepics.domainmodel.common.CallResult

interface AuthDataSource {
    suspend fun login(request: LoginRequest): CallResult<AuthResponse>
    suspend fun createCode(request: CreateCode): CallResult<String>
    suspend fun checkCode(request: CheckCodeRequest): CallResult<String>
    suspend fun createUser(request: CreateUserRequest): CallResult<AuthResponse>
    suspend fun refreshToken(request: TokenRefreshRequest): CallResult<AuthResponse>
}