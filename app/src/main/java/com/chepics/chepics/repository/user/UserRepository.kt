package com.chepics.chepics.repository.user

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserRepository {
    fun getUserId(): String
    suspend fun fetchUser(userId: String): CallResult<User>
}

internal class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val userStoreDataSource: UserStoreDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override fun getUserId(): String {
        return userStoreDataSource.getUserId()
    }

    override suspend fun fetchUser(userId: String): CallResult<User> {
        return withContext(ioDispatcher) {
            userDataSource.fetchUser(userId)
        }
    }
}