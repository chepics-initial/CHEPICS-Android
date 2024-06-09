package com.chepics.chepics.usecase.auth

import com.chepics.chepics.repository.auth.AuthRepository
import javax.inject.Inject

interface CompleteRegistrationUseCase {
    suspend fun skip()
}

internal class CompleteRegistrationUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : CompleteRegistrationUseCase {
    override suspend fun skip() {
        authRepository.skip()
    }
}