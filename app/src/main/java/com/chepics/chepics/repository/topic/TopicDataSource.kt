package com.chepics.chepics.repository.topic

import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult

interface TopicDataSource {
    suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>>
}