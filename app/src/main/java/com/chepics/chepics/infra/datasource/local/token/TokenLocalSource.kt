package com.chepics.chepics.infra.datasource.local.token

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.chepics.chepics.domainmodel.LocalAuthInfo
import com.chepics.chepics.infra.datasource.local.DataStoreType
import com.chepics.chepics.infra.ext.getStream
import com.chepics.chepics.infra.ext.save
import com.chepics.chepics.repository.token.TokenDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class TokenLocalSource @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenDataSource {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DataStoreType.TOKEN.value)
    private var localAuthInfo: MutableStateFlow<LocalAuthInfo> =
        MutableStateFlow(LocalAuthInfo(accessToken = "", isLoggedIn = false))

    override suspend fun storeToken(accessToken: String, refreshToken: String) {
        context.datastore.save(ACCESS_TOKEN_KEY, accessToken)
        localAuthInfo.value =
            LocalAuthInfo(accessToken = accessToken, isLoggedIn = localAuthInfo.value.isLoggedIn)
//        context.datastore.save(REFRESH_TOKEN_KEY, refreshToken)
        saveSecureValue(context = context, key = REFRESH_TOKEN_KEY, value = refreshToken)
    }

    override suspend fun removeToken() {
        context.datastore.save(ACCESS_TOKEN_KEY, "")
//        context.datastore.save(REFRESH_TOKEN_KEY, "")
        saveSecureValue(context = context, key = REFRESH_TOKEN_KEY, value = "")
        localAuthInfo.value = LocalAuthInfo(accessToken = "", isLoggedIn = false)
    }

    override fun observeAuthInfo(): Flow<LocalAuthInfo> {
        return localAuthInfo
    }

    override suspend fun setInitialAuthInfo() {
        localAuthInfo.value = LocalAuthInfo(
            accessToken = context.datastore.getStream(ACCESS_TOKEN_KEY, "").first(),
            isLoggedIn = context.datastore.getStream(ACCESS_TOKEN_KEY, "").first().isNotBlank()
        )
    }

    override suspend fun setLoginStatus(isLoggedIn: Boolean) {
        localAuthInfo.value =
            LocalAuthInfo(accessToken = localAuthInfo.value.accessToken, isLoggedIn = isLoggedIn)
    }

    override suspend fun getRefreshToken(): String {
//        return context.datastore.getStream(REFRESH_TOKEN_KEY, "").first()
        return getSecureValue(context = context, key = REFRESH_TOKEN_KEY) ?: ""
    }

    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            "encrypted_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun saveSecureValue(context: Context, key: String, value: String) {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    private fun getSecureValue(context: Context, key: String): String? {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        return sharedPreferences.getString(key, null)
    }

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")

        //        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }
}