package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.topic.TopicRepository
import javax.inject.Inject

interface TopicTopUseCase {
    suspend fun fetchTopic(topicId: String): CallResult<Topic>
    suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>>
}

internal class TopicTopUseCaseImpl @Inject constructor(
    private val topicRepository: TopicRepository,
    private val commentRepository: CommentRepository
) : TopicTopUseCase {
    override suspend fun fetchTopic(topicId: String): CallResult<Topic> {
        return topicRepository.fetchTopic(topicId)
    }

    override suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>> {
        return commentRepository.fetchSetComments(setId = setId, offset = offset)
    }

}