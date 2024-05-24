package com.chepics.chepics.usecase

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.set.SetRepository
import javax.inject.Inject

interface SetCommentUseCase {
    suspend fun fetchSet(setId: String): CallResult<PickSet>
    suspend fun fetchComments(setId: String, offset: Int?): CallResult<List<Comment>>
}

internal class SetCommentUseCaseImpl @Inject constructor(
    private val setRepository: SetRepository,
    private val commentRepository: CommentRepository
) : SetCommentUseCase {
    override suspend fun fetchSet(setId: String): CallResult<PickSet> {
        return setRepository.fetchSet(setId)
    }

    override suspend fun fetchComments(setId: String, offset: Int?): CallResult<List<Comment>> {
        return commentRepository.fetchSetComments(setId = setId, offset = offset)
    }

}