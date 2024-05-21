package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import javax.inject.Inject

interface CommentDetailUseCase {
    suspend fun fetchComment(id: String): CallResult<Comment>
    suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>>
}

internal class CommentDetailUseCaseImpl @Inject constructor(
    private val commentRepository: CommentRepository
) : CommentDetailUseCase {
    override suspend fun fetchComment(id: String): CallResult<Comment> {
        return commentRepository.fetchComment(id)
    }

    override suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>> {
        return commentRepository.fetchReplies(commentId = commentId, offset = offset)
    }

}