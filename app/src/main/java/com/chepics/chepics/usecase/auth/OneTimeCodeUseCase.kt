package com.chepics.chepics.usecase.auth

import com.chepics.chepics.domainmodel.CheckCodeRequest
import com.chepics.chepics.domainmodel.CreateCode
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthRepository
import javax.inject.Inject

interface OneTimeCodeUseCase {
    suspend fun verifyCode(email: String, code: String): CallResult<Unit>
    suspend fun createCode(email: String): CallResult<String>
}

internal class OneTimeCodeUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : OneTimeCodeUseCase {
    override suspend fun verifyCode(email: String, code: String): CallResult<Unit> {
        return authRepository.checkCode(CheckCodeRequest(email = email, code = code))
    }

    override suspend fun createCode(email: String): CallResult<String> {
        return authRepository.createCode(CreateCode(email))
    }
}