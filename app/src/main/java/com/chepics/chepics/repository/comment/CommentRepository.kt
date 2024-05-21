package com.chepics.chepics.repository.comment

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CommentRepository {
    suspend fun fetchFollowingComments(offset: Int?): CallResult<List<Comment>>
    suspend fun fetchUserComments(userId: String, offset: Int?): CallResult<List<Comment>>
    suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>>
    suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>>
    suspend fun fetchComment(id: String): CallResult<Comment>
}

internal class CommentRepositoryImpl @Inject constructor(
    private val commentDataSource: CommentDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : CommentRepository {
    override suspend fun fetchFollowingComments(offset: Int?): CallResult<List<Comment>> {
        return withContext(ioDispatcher) {
            commentDataSource.fetchFollowingComments(offset)
        }
    }

    override suspend fun fetchUserComments(
        userId: String,
        offset: Int?
    ): CallResult<List<Comment>> {
        return withContext(ioDispatcher) {
            commentDataSource.fetchUserComments(userId = userId, offset = offset)
        }
    }

    override suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>> {
        return withContext(ioDispatcher) {
            commentDataSource.fetchSetComments(setId = setId, offset = offset)
        }
    }

    override suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>> {
        return withContext(ioDispatcher) {
            commentDataSource.fetchReplies(commentId = commentId, offset = offset)
        }
    }

    override suspend fun fetchComment(id: String): CallResult<Comment> {
        return withContext(ioDispatcher) {
            commentDataSource.fetchComment(id)
        }
    }
}