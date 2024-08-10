package com.chepics.chepics.repository.user

import android.net.Uri
import com.chepics.chepics.domainmodel.FollowRequest
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult

interface UserDataSource {
    suspend fun fetchUser(userId: String): CallResult<User>
    suspend fun follow(request: FollowRequest): CallResult<Boolean>
    suspend fun updateUser(
        username: String,
        fullname: String,
        bio: String?,
        imageUri: Uri?
    ): CallResult<Unit>

    // TODO: - デバッグ用なのであとで削除
    suspend fun deleteUser(userId: String): CallResult<Unit>
}