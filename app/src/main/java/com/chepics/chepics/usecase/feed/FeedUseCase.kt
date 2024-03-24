package com.chepics.chepics.usecase.feed

import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.topic.TopicRepository
import javax.inject.Inject

interface FeedUseCase {
    suspend fun fetchFavoriteTopics(): CallResult<List<Topic>>
}

internal class FeedUseCaseImpl @Inject constructor(private val topicRepository: TopicRepository): FeedUseCase {
    override suspend fun fetchFavoriteTopics(): CallResult<List<Topic>> {
        return topicRepository.fetchFavoriteTopics()
    }
}