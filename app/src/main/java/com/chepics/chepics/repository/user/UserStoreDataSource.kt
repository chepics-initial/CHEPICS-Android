package com.chepics.chepics.repository.user

interface UserStoreDataSource {
    fun getUserId(): String
    fun storeUserId(userId: String)
}