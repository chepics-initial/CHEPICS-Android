package com.chepics.chepics.infra.datasource.local.token

import android.content.Context
import com.chepics.chepics.infra.datasource.local.DataStoreManager
import com.chepics.chepics.repository.token.TokenDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

internal class TokenLocalSource @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenDataSource {
    private val dataStore = DataStoreManager(context)
    private var accessTokenStream = MutableStateFlow("")
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    init {
        coroutineScope.launch {
            accessTokenStream = dataStore.getAccessToken() as MutableStateFlow<String>
        }
    }

    override suspend fun storeToken(accessToken: String, refreshToken: String) {
        dataStore.storeAccessToken(accessToken)
        // TODO: - Refresh Tokenの処理
    }

    override suspend fun removeToken() {
        dataStore.storeAccessToken("")
        // TODO: - Refresh Tokenの処理
    }

    override fun observeAccessToken(): StateFlow<String> {
        return accessTokenStream
    }
}