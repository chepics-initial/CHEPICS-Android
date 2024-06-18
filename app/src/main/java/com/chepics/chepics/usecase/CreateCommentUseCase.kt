package com.chepics.chepics.usecase

import android.net.Uri
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.comment.CommentRepository
import javax.inject.Inject

interface CreateCommentUseCase {
    suspend fun createComment(
        parentId: String?,
        topicId: String,
        setId: String,
        comment: String,
        link: String?,
        replyFor: List<String>?,
        images: List<Uri>?
    ): CallResult<Unit>
}

internal class CreateCommentUseCaseImpl @Inject constructor(
    private val commentRepository: CommentRepository
) : CreateCommentUseCase {
    override suspend fun createComment(
        parentId: String?,
        topicId: String,
        setId: String,
        comment: String,
        link: String?,
        replyFor: List<String>?,
        images: List<Uri>?
    ): CallResult<Unit> {
        return commentRepository.createComment(
            parentId = parentId,
            topicId = topicId,
            setId = setId,
            comment = comment,
            link = link,
            replyFor = replyFor,
            images = images
        )
    }
}