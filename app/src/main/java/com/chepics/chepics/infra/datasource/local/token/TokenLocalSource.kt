package com.chepics.chepics.infra.datasource.local.token

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.util.Calendar
import javax.crypto.Cipher
import javax.inject.Inject
import javax.security.auth.x500.X500Principal

internal class TokenLocalSource @Inject constructor(
    @ApplicationContext private val context: Context
) : TokenDataSource {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = DataStoreType.TOKEN.value)
    private val PROVIDER = "AndroidKeyStore"
    private val ALGORITHM = "RSA"
    private val CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    private var accessToken: MutableStateFlow<String> = MutableStateFlow("")

    override suspend fun storeToken(accessToken: String, refreshToken: String) {
        context.datastore.save(ACCESS_TOKEN_KEY, accessToken)
        val encryptedRefreshToken = encrypt(context, "refreshToken", refreshToken)
        context.datastore.save(REFRESH_TOKEN_KEY, encryptedRefreshToken)
    }

    override suspend fun removeToken() {
        context.datastore.save(ACCESS_TOKEN_KEY, "")
        context.datastore.save(REFRESH_TOKEN_KEY, "")
        accessToken.value = ""
    }

    override fun observeAccessToken(): Flow<String> {
        return accessToken
    }

    override suspend fun setAccessToken() {
        accessToken.value = context.datastore.getStream(ACCESS_TOKEN_KEY, "").first()
    }

    /**
     * テキストを暗号化する
     * @param context
     * @param alias キーペアを識別するためのエリアス。用途ごとに一意にする。
     * @param plainText 暗号化したいテキスト
     * @return 暗号化されBase64でラップされた文字列
     */
    private fun encrypt(context: Context, alias: String, plainText: String): String {
        val keyStore = KeyStore.getInstance(PROVIDER)
        keyStore.load(null)

        // キーペアがない場合生成
        if (!keyStore.containsAlias(alias)) {
            val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER)
            keyPairGenerator.initialize(createKeyGenParameterSpec(context, alias))
            keyPairGenerator.generateKeyPair()
        }
        val publicKey = keyStore.getCertificate(alias).getPublicKey()
        val privateKey = keyStore.getKey(alias, null)

        // 公開鍵で暗号化
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val bytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // SharedPreferencesに保存しやすいようにBase64でString化
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    /**
     * 暗号化されたテキストを復号化する
     * @param alias キーペアを識別するためのエリアス。用途ごとに一意にする。
     * @param encryptedText encryptで暗号化されたテキスト
     * @return 復号化された文字列
     */
    fun decrypt(alias: String, encryptedText: String): String? {
        val keyStore = KeyStore.getInstance(PROVIDER)
        keyStore.load(null)
        if (!keyStore.containsAlias(alias)) {
            return null
        }

        // 秘密鍵で復号化
        val privateKey = keyStore.getKey(alias, null)
        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, privateKey)
        val bytes = Base64.decode(encryptedText, Base64.DEFAULT)

        val b = cipher.doFinal(bytes)
        return String(b)
    }

    /**
     * キーペアを生成する
     */
    private fun createKeyGenParameterSpec(context: Context, alias: String): KeyGenParameterSpec {
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 100)

        return KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setCertificateSubject(X500Principal("CN=$alias"))
            .setCertificateSerialNumber(BigInteger.valueOf(1000000))
            .setCertificateNotBefore(start.time)
            .setCertificateNotAfter(end.time)
            .setKeySize(2048)
            .build()
    }

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }
}