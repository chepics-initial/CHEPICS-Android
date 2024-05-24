package com.chepics.chepics.repository.set

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.CreateSetRequest
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.PickSetRequest
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SetRepository {
    suspend fun fetchSets(topicId: String): CallResult<List<PickSet>>
    suspend fun createSet(body: CreateSetRequest): CallResult<Unit>
    suspend fun pickSet(body: PickSetRequest): CallResult<PickSet>
    suspend fun fetchSet(setId: String): CallResult<PickSet>
}

internal class SetRepositoryImpl @Inject constructor(
    private val setDataSource: SetDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SetRepository {
    override suspend fun fetchSets(topicId: String): CallResult<List<PickSet>> {
        return withContext(ioDispatcher) {
            setDataSource.fetchSets(topicId)
        }
    }

    override suspend fun createSet(body: CreateSetRequest): CallResult<Unit> {
        return withContext(ioDispatcher) {
            setDataSource.createSet(body)
        }
    }

    override suspend fun pickSet(body: PickSetRequest): CallResult<PickSet> {
        return withContext(ioDispatcher) {
            setDataSource.pickSet(body)
        }
    }

    override suspend fun fetchSet(setId: String): CallResult<PickSet> {
        return withContext(ioDispatcher) {
            setDataSource.fetchSet(setId)
        }
    }
}