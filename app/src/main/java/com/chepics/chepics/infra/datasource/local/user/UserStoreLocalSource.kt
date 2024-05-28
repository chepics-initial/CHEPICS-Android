package com.chepics.chepics.infra.datasource.local.user

import android.content.Context
import com.chepics.chepics.infra.datasource.local.DataStoreManager
import com.chepics.chepics.repository.user.UserStoreDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class UserStoreLocalSource @Inject constructor(
    @ApplicationContext private val context: Context
) : UserStoreDataSource {
    private val dataStore = DataStoreManager(context)
    override fun getUserId(): String {
        return dataStore.getUserIdBlocking() ?: ""
    }

    override suspend fun storeUserId(userId: String) {
        dataStore.storeUserId(userId)
    }
}