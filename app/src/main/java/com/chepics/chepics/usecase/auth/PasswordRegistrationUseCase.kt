package com.chepics.chepics.usecase.auth

import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthRepository
import javax.inject.Inject

interface PasswordRegistrationUseCase {
    suspend fun registerPassword(password: String): CallResult<Unit>
}

internal class PasswordRegistrationUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : PasswordRegistrationUseCase {
    override suspend fun registerPassword(password: String): CallResult<Unit> {
        return authRepository.createUser(password)
    }
}