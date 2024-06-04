package com.chepics.chepics.infra.datasource.api.user

import com.chepics.chepics.domainmodel.FollowRequest
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.repository.user.UserDataSource
import javax.inject.Inject

class UserRemoteSource @Inject constructor(private val api: UserApi) : UserDataSource {
    override suspend fun fetchUser(userId: String): CallResult<User> {
        return safeApiCall { api.fetchUser(userId) }
    }

    override suspend fun follow(request: FollowRequest): CallResult<Boolean> {
        return safeApiCall { api.follow(request) }.mapSuccess { it.isFollow }
    }
}