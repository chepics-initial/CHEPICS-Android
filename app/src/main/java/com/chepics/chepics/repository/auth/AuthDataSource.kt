package com.chepics.chepics.repository.auth

import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.common.CallResult

interface AuthDataSource {
    suspend fun login(request: LoginRequest): CallResult<Unit>
    suspend fun createCode(email: String): CallResult<String>
}