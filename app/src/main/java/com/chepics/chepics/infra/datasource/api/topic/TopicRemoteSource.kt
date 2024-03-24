package com.chepics.chepics.infra.datasource.api.topic

import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.mock.mockTopic1
import com.chepics.chepics.repository.topic.TopicDataSource
import javax.inject.Inject

class TopicRemoteSource @Inject constructor(private val api: TopicApi): TopicDataSource {
    override suspend fun fetchFavoriteTopics(): CallResult<List<Topic>> {
        return CallResult.Success(data = listOf(mockTopic1))
    }
}