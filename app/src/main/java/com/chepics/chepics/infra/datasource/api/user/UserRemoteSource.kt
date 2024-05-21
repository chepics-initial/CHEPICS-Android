package com.chepics.chepics.infra.datasource.api.user

import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.mock.mockUser1
import com.chepics.chepics.repository.user.UserDataSource
import javax.inject.Inject

class UserRemoteSource @Inject constructor(private val api: UserApi): UserDataSource {
    override suspend fun fetchUser(userId: String): CallResult<User> {
        return CallResult.Success(mockUser1)
    }
}