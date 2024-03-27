package com.chepics.chepics.infra.datasource.local.header

import com.chepics.chepics.domainmodel.net.RequestHeaderKey
import com.chepics.chepics.repository.net.RequestHeaderDataSource
import okhttp3.internal.toImmutableMap

object RequestHeaderCacheSource: RequestHeaderDataSource {
    private var headerMap = mutableMapOf<String, String>()

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