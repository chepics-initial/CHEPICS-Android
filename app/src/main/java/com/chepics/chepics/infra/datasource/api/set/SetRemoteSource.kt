package com.chepics.chepics.infra.datasource.api.set

import com.chepics.chepics.domainmodel.CreateSetRequest
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.PickSetRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.mock.mockSet1
import com.chepics.chepics.mock.mockSet2
import com.chepics.chepics.mock.mockSet3
import com.chepics.chepics.repository.set.SetDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject

class SetRemoteSource @Inject constructor(private val setApi: SetApi) : SetDataSource {
    override suspend fun fetchSets(topicId: String): CallResult<List<PickSet>> {
        return CallResult.Success(data = listOf(mockSet1, mockSet2, mockSet3))
    }

    override suspend fun createSet(body: CreateSetRequest): CallResult<Unit> {
        return CallResult.Success(Unit)
    }

    override suspend fun pickSet(body: PickSetRequest): CallResult<PickSet> {
        delay(1000L)
        return CallResult.Success(mockSet1)
    }
}