package com.chepics.chepics.infra.datasource.api.topic

import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.mock.mockTopic1
import com.chepics.chepics.mock.mockTopic10
import com.chepics.chepics.mock.mockTopic11
import com.chepics.chepics.mock.mockTopic12
import com.chepics.chepics.mock.mockTopic13
import com.chepics.chepics.mock.mockTopic14
import com.chepics.chepics.mock.mockTopic15
import com.chepics.chepics.mock.mockTopic2
import com.chepics.chepics.mock.mockTopic3
import com.chepics.chepics.mock.mockTopic4
import com.chepics.chepics.mock.mockTopic5
import com.chepics.chepics.mock.mockTopic6
import com.chepics.chepics.mock.mockTopic7
import com.chepics.chepics.mock.mockTopic8
import com.chepics.chepics.mock.mockTopic9
import com.chepics.chepics.repository.topic.TopicDataSource
import javax.inject.Inject

class TopicRemoteSource @Inject constructor(private val api: TopicApi): TopicDataSource {
    override suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>> {
        return CallResult.Success(data = listOf(
            mockTopic1,
            mockTopic2,
            mockTopic3,
            mockTopic4,
            mockTopic5,
            mockTopic6,
            mockTopic7,
            mockTopic8,
            mockTopic9,
            mockTopic10,
            mockTopic11,
            mockTopic12,
            mockTopic13,
            mockTopic14,
            mockTopic15
        ))
    }
}