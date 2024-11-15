package com.chepics.chepics.usecase.feed

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.LikeRequest
import com.chepics.chepics.domainmodel.LikeResponse
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.topic.TopicRepository
import javax.inject.Inject

interface FeedUseCase {
    suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>>
    suspend fun fetchComments(offset: Int?): CallResult<List<Comment>>
    suspend fun like(setId: String, commentId: String): CallResult<LikeResponse>
}

internal class FeedUseCaseImpl @Inject constructor(
    private val topicRepository: TopicRepository,
    private val commentRepository: CommentRepository
) : FeedUseCase {
    override suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>> {
        return topicRepository.fetchFavoriteTopics(offset)
    }

    override suspend fun fetchComments(offset: Int?): CallResult<List<Comment>> {
        return commentRepository.fetchFollowingComments(offset)
    }

    override suspend fun like(setId: String, commentId: String): CallResult<LikeResponse> {
        return commentRepository.like(LikeRequest(setId = setId, commentId = commentId))
    }
}