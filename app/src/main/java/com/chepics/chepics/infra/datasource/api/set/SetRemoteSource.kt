package com.chepics.chepics.infra.datasource.api.set

import com.chepics.chepics.domainmodel.CreateSetRequest
import com.chepics.chepics.domainmodel.MySet
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.PickSetRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.repository.set.SetDataSource
import javax.inject.Inject

class SetRemoteSource @Inject constructor(private val api: SetApi) : SetDataSource {
    override suspend fun fetchSets(topicId: String, offset: Int?): CallResult<List<PickSet>> {
        return safeApiCall {
            api.fetchSets(
                topicId = topicId,
                offset = offset
            )
        }.mapSuccess { it.items }
    }

    override suspend fun createSet(body: CreateSetRequest): CallResult<Unit> {
        return safeApiCall { api.createSet(body) }.mapSuccess { }
    }

    override suspend fun pickSet(body: PickSetRequest): CallResult<PickSet> {
        return safeApiCall { api.pickSet(body) }
    }

    override suspend fun fetchSet(setId: String): CallResult<PickSet> {
        return safeApiCall { api.fetchSet(setId) }
    }

    override suspend fun fetchPickedSets(offset: Int?): CallResult<List<MySet>> {
        return safeApiCall { api.fetchPickedSets(offset) }.mapSuccess { it.items }
    }

    override suspend fun fetchPickedSet(topicId: String): CallResult<PickSet> {
        return safeApiCall { api.fetchPickedSet(topicId) }
    }
}