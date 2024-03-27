package com.chepics.chepics.repository.net

import com.chepics.chepics.domainmodel.net.RequestHeaderKey

interface RequestHeaderDataSource {
    suspend fun updateHeader(header: RequestHeaderKey, value: String)
    suspend fun removeHeader(header: RequestHeaderKey)
    suspend fun setHeaders(headers: Map<String, String>)
    fun getHeaders(): Map<String, String>
}