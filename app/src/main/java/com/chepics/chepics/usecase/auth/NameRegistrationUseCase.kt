package com.chepics.chepics.usecase.auth

import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.user.UserRepository
import javax.inject.Inject

interface NameRegistrationUseCase {
    suspend fun registerName(username: String, fullname: String): CallResult<Unit>
}

internal class NameRegistrationUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : NameRegistrationUseCase {
    override suspend fun registerName(username: String, fullname: String): CallResult<Unit> {
        return userRepository.updateUser(
            username = username,
            fullname = fullname,
            bio = null,
            imageUri = null
        )
    }
}