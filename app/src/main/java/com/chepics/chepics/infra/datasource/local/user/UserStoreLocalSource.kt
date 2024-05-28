package com.chepics.chepics.infra.datasource.local.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chepics.chepics.infra.datasource.local.DataStoreType
import com.chepics.chepics.infra.ext.getStream
import com.chepics.chepics.infra.ext.save
import com.chepics.chepics.repository.user.UserStoreDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

internal class UserStoreLocalSource @Inject constructor(
    @ApplicationContext private val context: Context
) : UserStoreDataSource {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DataStoreType.USER.value)
    override fun getUserId(): String {
        return runBlocking {
            context.datastore.getStream(USER_ID_KEY, "").first()
        }
    }

    override suspend fun storeUserId(userId: String) {
        context.datastore.save(USER_ID_KEY, userId)
    }

    private companion object {
        private val USER_ID_KEY = stringPreferencesKey("user_id")
    }
}