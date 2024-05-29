package com.chepics.chepics.repository.net

import com.chepics.chepics.domainmodel.net.RequestHeaderKey
import com.chepics.chepics.repository.token.TokenDataSource
import okhttp3.internal.toImmutableMap
import javax.inject.Inject

interface RequestHeaderRepository {
    fun updateHeader(header: RequestHeaderKey, value: String)
    fun removeHeader(header: RequestHeaderKey)

    fun setHeaders(headers: Map<String, String>)

    fun getHeaders(): Map<String, String>
}

internal class RequestHeaderRepositoryImpl @Inject constructor() : RequestHeaderRepository {
    private var headerMap = mutableMapOf<String, String>()

    override fun updateHeader(header: RequestHeaderKey, value: String) {
        headerMap[header.key] = value
    }

    override fun removeHeader(header: RequestHeaderKey) {
        if (headerMap.containsKey(header.key)) {
            headerMap.remove(header.key)
        }
    }

    override fun setHeaders(headers: Map<String, String>) {
        headerMap = headers.toMutableMap()
    }

    override fun getHeaders(): Map<String, String> {
        return headerMap.toImmutableMap()
    }
}