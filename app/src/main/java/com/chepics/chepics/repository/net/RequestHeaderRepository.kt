package com.chepics.chepics.repository.net

import com.chepics.chepics.domainmodel.net.RequestHeaderKey
import com.chepics.chepics.repository.token.TokenDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.internal.toImmutableMap
import javax.inject.Inject

interface RequestHeaderRepository {
    suspend fun updateHeader(header: RequestHeaderKey, value: String)
    suspend fun removeHeader(header: RequestHeaderKey)

    suspend fun setHeaders(headers: Map<String, String>)

    fun getHeaders(): Map<String, String>
}

internal class RequestHeaderRepositoryImpl @Inject constructor(
    private val tokenDataSource: TokenDataSource
) : RequestHeaderRepository {
    private var headerMap = mutableMapOf<String, String>()
    private val job = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    init {
        coroutineScope.launch {
            tokenDataSource.observeAccessToken().collect {
                setHeaders(mapOf(RequestHeaderKey.AUTHORIZATION_TOKEN.key to "Bearer $it"))
            }
        }
    }

    override suspend fun updateHeader(header: RequestHeaderKey, value: String) {
        headerMap[header.key] = value
    }

    override suspend fun removeHeader(header: RequestHeaderKey) {
        if (headerMap.containsKey(header.key)) {
            headerMap.remove(header.key)
        }
    }

    override suspend fun setHeaders(headers: Map<String, String>) {
        headerMap = headers.toMutableMap()
    }

    override fun getHeaders(): Map<String, String> {
        return headerMap.toImmutableMap()
    }
}