package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.UserData
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthRepository
import com.chepics.chepics.repository.user.UserRepository
import javax.inject.Inject

interface MyPageTopUseCase {
    fun getUserData(): UserData?
    suspend fun fetchUser(): CallResult<User>
    suspend fun logout()
}

internal class MyPageTopUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
) : MyPageTopUseCase {
    override fun getUserData(): UserData? {
        return userRepository.getUserData()
    }

    override suspend fun fetchUser(): CallResult<User> {
        return userRepository.fetchUser(userRepository.getUserId())
    }

    override suspend fun logout() {
        authRepository.logout()
    }
}