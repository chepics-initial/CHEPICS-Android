package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.LikeRequest
import com.chepics.chepics.domainmodel.LikeResponse
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.set.SetRepository
import javax.inject.Inject

interface SetCommentDetailUseCase {
    suspend fun fetchSet(setId: String): CallResult<PickSet>
    suspend fun fetchComment(commentId: String): CallResult<Comment>
    suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>>
    suspend fun like(setId: String, commentId: String): CallResult<LikeResponse>
}

internal class SetCommentDetailUseCaseImpl @Inject constructor(
    private val setRepository: SetRepository,
    private val commentRepository: CommentRepository
) : SetCommentDetailUseCase {
    override suspend fun fetchSet(setId: String): CallResult<PickSet> {
        return setRepository.fetchSet(setId)
    }

    override suspend fun fetchComment(commentId: String): CallResult<Comment> {
        return commentRepository.fetchComment(commentId)
    }

    override suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>> {
        return commentRepository.fetchReplies(commentId = commentId, offset = offset)
    }

    override suspend fun like(setId: String, commentId: String): CallResult<LikeResponse> {
        return commentRepository.like(LikeRequest(setId = setId, commentId = commentId))
    }
}