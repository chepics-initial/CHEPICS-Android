package com.chepics.chepics.infra.datasource.api.comment

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.mock.mockComment1
import com.chepics.chepics.mock.mockComment2
import com.chepics.chepics.mock.mockComment3
import com.chepics.chepics.mock.mockComment4
import com.chepics.chepics.mock.mockComment5
import com.chepics.chepics.mock.mockComment6
import com.chepics.chepics.repository.comment.CommentDataSource
import kotlinx.coroutines.delay
import javax.inject.Inject

class CommentRemoteSource @Inject constructor(private val api: CommentApi) : CommentDataSource {
    override suspend fun fetchFollowingComments(offset: Int?): CallResult<List<Comment>> {
        delay(1000L)
        return CallResult.Success(
            data = listOf(
                mockComment1,
                mockComment2,
                mockComment3,
                mockComment4,
                mockComment5,
                mockComment6
            )
        )
    }

    override suspend fun fetchUserComments(
        userId: String,
        offset: Int?
    ): CallResult<List<Comment>> {
        delay(1000L)
        return CallResult.Success(
            data = listOf(
                mockComment1,
                mockComment2,
                mockComment3,
                mockComment4,
                mockComment5,
                mockComment6
            )
        )
    }

    override suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>> {
        delay(1000L)
        return CallResult.Success(
            data = listOf(
                mockComment1,
                mockComment2,
                mockComment3,
                mockComment4,
                mockComment5,
                mockComment6
            )
        )
    }

    override suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>> {
        delay(1000L)
        return CallResult.Success(
            data = listOf(
                mockComment1,
                mockComment2,
                mockComment3,
                mockComment4,
                mockComment5,
                mockComment6
            )
        )
    }

    override suspend fun fetchComment(id: String): CallResult<Comment> {
        delay(1000L)
        return CallResult.Success(mockComment2)
    }
}