package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.LikeRequest
import com.chepics.chepics.domainmodel.LikeResponse
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.EMPTY_RESPONSE
import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.set.SetRepository
import javax.inject.Inject

interface CommentDetailUseCase {
    suspend fun fetchComment(id: String): CallResult<Comment>
    suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>>
    suspend fun like(setId: String, commentId: String): CallResult<LikeResponse>
    suspend fun isSetPicked(topicId: String): CallResult<Boolean>
}

internal class CommentDetailUseCaseImpl @Inject constructor(
    private val commentRepository: CommentRepository,
    private val setRepository: SetRepository
) : CommentDetailUseCase {
    override suspend fun fetchComment(id: String): CallResult<Comment> {
        return commentRepository.fetchComment(id)
    }

    override suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>> {
        return commentRepository.fetchReplies(commentId = commentId, offset = offset)
    }

    override suspend fun like(setId: String, commentId: String): CallResult<LikeResponse> {
        return commentRepository.like(LikeRequest(setId = setId, commentId = commentId))
    }

    override suspend fun isSetPicked(topicId: String): CallResult<Boolean> {
        when (val result = setRepository.fetchPickedSet(topicId)) {
            is CallResult.Success -> return CallResult.Success(true)
            is CallResult.Error -> {
                if (result.exception.message == EMPTY_RESPONSE) {
                    return CallResult.Success(false)
                }
                return result
            }
        }
    }
}