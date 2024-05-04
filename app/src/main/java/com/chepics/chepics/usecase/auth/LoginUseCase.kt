package com.chepics.chepics.usecase.auth

import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthRepository
import javax.inject.Inject

interface LoginUseCase {
    suspend fun login(email: String, password: String): CallResult<Unit>
}

internal class LoginUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
): LoginUseCase {
    override suspend fun login(email: String, password: String): CallResult<Unit> {
        return authRepository.login(LoginRequest(email = email, password = password))
    }
}