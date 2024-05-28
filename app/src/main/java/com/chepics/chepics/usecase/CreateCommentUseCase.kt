package com.chepics.chepics.usecase

import com.chepics.chepics.repository.comment.CommentRepository
import javax.inject.Inject

interface CreateCommentUseCase {
}

internal class CreateCommentUseCaseImpl @Inject constructor(
    private val commentRepository: CommentRepository
) : CreateCommentUseCase {

}