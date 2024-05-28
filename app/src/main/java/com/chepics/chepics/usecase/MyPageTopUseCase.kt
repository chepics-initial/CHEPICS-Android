package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthRepository
import com.chepics.chepics.repository.user.UserRepository
import javax.inject.Inject

interface MyPageTopUseCase {
    suspend fun fetchUser(): CallResult<User>
    suspend fun logout()
}

internal class MyPageTopUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : MyPageTopUseCase {
    override suspend fun fetchUser(): CallResult<User> {
        return userRepository.fetchUser(userRepository.getUserId())
    }

    override suspend fun logout() {
        authRepository.logout()
    }
}