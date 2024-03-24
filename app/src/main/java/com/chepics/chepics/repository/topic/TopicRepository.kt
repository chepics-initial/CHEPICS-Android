package com.chepics.chepics.repository.topic

import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TopicRepository {
    suspend fun fetchFavoriteTopics(): CallResult<List<Topic>>
}

internal class TopicRepositoryImpl @Inject constructor(
    private val topicDataSource: TopicDataSource,
//    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): TopicRepository {
    // TODO: - 使えたらioDispatcherを使う方がいいはずなんやと思う（多分）
    override suspend fun fetchFavoriteTopics(): CallResult<List<Topic>> {
        return topicDataSource.fetchFavoriteTopics()
    }
}