package com.chepics.chepics.repository.comment

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.common.CallResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface CommentRepository {
    suspend fun fetchFollowingComments(offset: Int?): CallResult<List<Comment>>
}

internal class CommentRepositoryImpl @Inject constructor(
    private val commentDataSource: CommentDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): CommentRepository {
    override suspend fun fetchFollowingComments(offset: Int?): CallResult<List<Comment>> {
        return withContext(ioDispatcher) {
            commentDataSource.fetchFollowingComments(offset)
        }
    }

}