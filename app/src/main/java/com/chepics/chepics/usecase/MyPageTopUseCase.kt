package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.mock.mockUser1
import com.chepics.chepics.repository.user.UserRepository
import javax.inject.Inject

interface MyPageTopUseCase {
    suspend fun fetchUser(): CallResult<User>
}

internal class MyPageTopUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : MyPageTopUseCase {
    override suspend fun fetchUser(): CallResult<User> {
        return userRepository.fetchUser(userRepository.getUserId())
    }
}