package com.chepics.chepics.repository.topic

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TopicRepository {
    suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>>
    suspend fun fetchUserTopics(userId: String, offset: Int?): CallResult<List<Topic>>
}

internal class TopicRepositoryImpl @Inject constructor(
    private val topicDataSource: TopicDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): TopicRepository {
    override suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>> {
        return withContext(ioDispatcher) {
            topicDataSource.fetchFavoriteTopics(offset)
        }
    }

    override suspend fun fetchUserTopics(userId: String, offset: Int?): CallResult<List<Topic>> {
        return withContext(ioDispatcher) {
            topicDataSource.fetchUserTopics(userId = userId, offset = offset)
        }
    }
}