package com.chepics.chepics.infra.datasource.api.topic

import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.repository.topic.TopicDataSource
import javax.inject.Inject

class TopicRemoteSource @Inject constructor(private val api: TopicApi) : TopicDataSource {
    override suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>> {
        return safeApiCall { api.fetchFavoriteTopics(offset) }.mapSuccess { it.items }
    }

    override suspend fun fetchUserTopics(userId: String, offset: Int?): CallResult<List<Topic>> {
        return safeApiCall {
            api.fetchUserTopics(
                userId = userId,
                offset = offset
            )
        }.mapSuccess { it.items }
    }

    override suspend fun fetchTopic(topicId: String): CallResult<Topic> {
        return safeApiCall { api.fetchTopic(topicId) }
    }
}