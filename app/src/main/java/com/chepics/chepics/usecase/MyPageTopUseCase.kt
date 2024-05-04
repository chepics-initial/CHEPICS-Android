package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.mock.mockUser1
import javax.inject.Inject

interface MyPageTopUseCase {
    suspend fun fetchUser(): CallResult<User>
}

internal class MyPageTopUseCaseImpl @Inject constructor(
): MyPageTopUseCase {
    override suspend fun fetchUser(): CallResult<User> {
        return CallResult.Success(mockUser1)
    }

}