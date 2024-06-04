package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.PickSetRequest
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.set.SetRepository
import com.chepics.chepics.repository.topic.TopicRepository
import javax.inject.Inject

interface TopicTopUseCase {
    suspend fun fetchTopic(topicId: String): CallResult<Topic>
    suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>>
    suspend fun fetchSets(topicId: String, offset: Int?): CallResult<List<PickSet>>
    suspend fun pickSet(topicId: String, setId: String): CallResult<PickSet>
}

internal class TopicTopUseCaseImpl @Inject constructor(
    private val topicRepository: TopicRepository,
    private val commentRepository: CommentRepository,
    private val setRepository: SetRepository
) : TopicTopUseCase {
    override suspend fun fetchTopic(topicId: String): CallResult<Topic> {
        return topicRepository.fetchTopic(topicId)
    }

    override suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>> {
        return commentRepository.fetchSetComments(setId = setId, offset = offset)
    }

    override suspend fun fetchSets(topicId: String, offset: Int?): CallResult<List<PickSet>> {
        return setRepository.fetchSets(topicId = topicId, offset = offset)
    }

    override suspend fun pickSet(topicId: String, setId: String): CallResult<PickSet> {
        return setRepository.pickSet(PickSetRequest(topicId = topicId, setId = setId))
    }
}