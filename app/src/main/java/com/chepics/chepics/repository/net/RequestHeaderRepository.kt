package com.chepics.chepics.repository.net

import com.chepics.chepics.domainmodel.net.RequestHeaderKey
import javax.inject.Inject

interface RequestHeaderRepository {
    suspend fun updateHeader(header: RequestHeaderKey, value: String)
    suspend fun removeHeader(header: RequestHeaderKey)

    suspend fun setHeaders(headers: Map<String, String>)

    fun getHeaders(): Map<String, String>
}

internal class RequestHeaderRepositoryImpl @Inject constructor(
    private val dataSource: RequestHeaderDataSource
) : RequestHeaderRepository {

    override suspend fun updateHeader(header: RequestHeaderKey, value: String) {
        dataSource.updateHeader(header, value)
    }

    override suspend fun removeHeader(header: RequestHeaderKey) {
        dataSource.removeHeader(header)
    }

    override suspend fun setHeaders(headers: Map<String, String>) {
        dataSource.setHeaders(headers)
    }

    override fun getHeaders(): Map<String, String> {
        return dataSource.getHeaders()
    }
}