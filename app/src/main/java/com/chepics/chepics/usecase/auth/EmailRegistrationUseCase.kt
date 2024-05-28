package com.chepics.chepics.usecase.auth

import com.chepics.chepics.domainmodel.CreateCode
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthRepository
import javax.inject.Inject

interface EmailRegistrationUseCase {
    suspend fun createCode(email: String): CallResult<String>
}

internal class EmailRegistrationUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : EmailRegistrationUseCase {
    override suspend fun createCode(email: String): CallResult<String> {
        return authRepository.createCode(CreateCode(email))
    }
}