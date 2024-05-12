package com.chepics.chepics.infra.datasource.api.auth

import com.chepics.chepics.domainmodel.CheckCodeRequest
import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthDataSource
import javax.inject.Inject

class AuthRemoteSource @Inject constructor(private val api: AuthApi) : AuthDataSource {
    override suspend fun login(request: LoginRequest): CallResult<Unit> {
        return CallResult.Success(Unit)
    }

    override suspend fun createCode(email: String): CallResult<String> {
        return CallResult.Success("google@gmail.com")
    }

    override suspend fun checkCode(request: CheckCodeRequest): CallResult<String> {
        return CallResult.Success("google@gmail.com")
    }
}