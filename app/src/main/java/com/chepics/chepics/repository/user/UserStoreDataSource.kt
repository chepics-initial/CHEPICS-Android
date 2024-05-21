package com.chepics.chepics.repository.user

interface UserStoreDataSource {
    fun getUserId(): String
    suspend fun storeUserId(userId: String)
}