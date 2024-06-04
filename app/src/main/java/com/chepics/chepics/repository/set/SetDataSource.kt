package com.chepics.chepics.repository.set

import com.chepics.chepics.domainmodel.CreateSetRequest
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.PickSetRequest
import com.chepics.chepics.domainmodel.common.CallResult

interface SetDataSource {
    suspend fun fetchSets(topicId: String, offset: Int?): CallResult<List<PickSet>>
    suspend fun createSet(body: CreateSetRequest): CallResult<Unit>
    suspend fun pickSet(body: PickSetRequest): CallResult<PickSet>
    suspend fun fetchSet(setId: String): CallResult<PickSet>
}