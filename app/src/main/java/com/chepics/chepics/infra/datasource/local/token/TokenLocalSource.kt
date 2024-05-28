package com.chepics.chepics.infra.datasource.local.token

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.chepics.chepics.infra.datasource.local.DataStoreType
import com.chepics.chepics.infra.ext.getStream
import com.chepics.chepics.infra.ext.save
import com.chepics.chepics.repository.token.TokenDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class TokenLocalSource @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenDataSource {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DataStoreType.TOKEN.value)

    override suspend fun storeToken(accessToken: String, refreshToken: String) {
        context.datastore.save(ACCESS_TOKEN_KEY, accessToken)
        // TODO: - Refresh Tokenの処理
    }

    override suspend fun removeToken() {
        context.datastore.save(ACCESS_TOKEN_KEY, "")
        // TODO: - Refresh Tokenの処理
    }

    override fun observeAccessToken(): Flow<String> {
        return context.datastore.getStream(ACCESS_TOKEN_KEY, "")
    }

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }
}