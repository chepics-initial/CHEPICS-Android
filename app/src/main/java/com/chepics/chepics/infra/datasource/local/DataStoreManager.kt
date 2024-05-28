package com.chepics.chepics.infra.datasource.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

class DataStoreManager(context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "TOKEN_KEY")
    private val dataStore = context.dataStore

    companion object {
        val userIdKey = stringPreferencesKey("USER_ID_KEY")
    }

    suspend fun storeUserId(userId: String) {
        dataStore.edit { pref ->
            pref[userIdKey] = userId
        }
    }

    private fun getUserId(): Flow<String?> {
        return dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { pref ->
                pref[userIdKey]
            }
    }

    fun getUserIdBlocking(): String? {
        return runBlocking {
            getUserId().first()
        }
    }
}