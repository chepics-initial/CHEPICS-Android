package com.chepics.chepics.repository.topic

import android.net.Uri
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult

interface TopicDataSource {
    suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>>
    suspend fun fetchUserTopics(userId: String, offset: Int?): CallResult<List<Topic>>
    suspend fun fetchTopic(topicId: String): CallResult<Topic>
    suspend fun createTopic(
        title: String,
        link: String?,
        description: String?,
        images: List<Uri>?
    ): CallResult<Unit>
}