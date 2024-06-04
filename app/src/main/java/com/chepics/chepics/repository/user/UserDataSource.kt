package com.chepics.chepics.repository.user

import com.chepics.chepics.domainmodel.FollowRequest
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult

interface UserDataSource {
    suspend fun fetchUser(userId: String): CallResult<User>
    suspend fun follow(request: FollowRequest): CallResult<Boolean>
}