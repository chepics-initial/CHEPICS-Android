package com.chepics.chepics.repository.user

import com.chepics.chepics.domainmodel.UserData

interface UserStoreDataSource {
    fun getUserId(): String
    suspend fun storeUserId(userId: String)
    fun storeUserData(data: UserData)
    fun getUserData(): UserData?
}