package com.chepics.chepics.infra.datasource.api.comment

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.repository.comment.CommentDataSource
import javax.inject.Inject

class CommentRemoteSource @Inject constructor(private val api: CommentApi) : CommentDataSource {
    override suspend fun fetchFollowingComments(offset: Int?): CallResult<List<Comment>> {
        return safeApiCall { api.fetchFavoriteTopics(offset) }.mapSuccess { it.items }
    }

    override suspend fun fetchUserComments(
        userId: String,
        offset: Int?
    ): CallResult<List<Comment>> {
        return safeApiCall {
            api.fetchUserComments(
                userId = userId,
                offset = offset
            )
        }.mapSuccess { it.items }
    }

    override suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>> {
        return safeApiCall { api.fetchSetComments(setId, offset) }.mapSuccess { it.items }
    }

    override suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>> {
        return safeApiCall { api.fetchReplies(commentId, offset) }.mapSuccess { it.items }
    }

    override suspend fun fetchComment(id: String): CallResult<Comment> {
        return safeApiCall { api.fetchComment(id) }
    }
}