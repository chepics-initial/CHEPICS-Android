package com.chepics.chepics.repository.user

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserRepository {
    suspend fun fetchUser(userId: String): CallResult<User>
}

internal class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): UserRepository {
    override suspend fun fetchUser(userId: String): CallResult<User> {
        return withContext(ioDispatcher) {
            userDataSource.fetchUser(userId)
        }
    }
}