package com.chepics.chepics.repository.comment

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthDataSource
import com.chepics.chepics.repository.token.TokenDataSource
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
    private val tokenDataSource: TokenDataSource,
    private val authDataSource: AuthDataSource,
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
        return handleResponse(commentDataSource.fetchUserComments(userId = userId, offset = offset))
    }

    override suspend fun fetchSetComments(setId: String, offset: Int?): CallResult<List<Comment>> {
        return handleResponse(commentDataSource.fetchSetComments(setId = setId, offset = offset))
    }

    override suspend fun fetchReplies(commentId: String, offset: Int?): CallResult<List<Comment>> {
        return handleResponse(
            commentDataSource.fetchReplies(
                commentId = commentId,
                offset = offset
            )
        )
    }

    override suspend fun fetchComment(id: String): CallResult<Comment> {
        return handleResponse(commentDataSource.fetchComment(id))
    }

    private suspend fun <T : Any> handleResponse(response: CallResult<T>): CallResult<T> {
        val result = withContext(ioDispatcher) {
            response
        }

        when (result) {
            is CallResult.Success -> return result
            is CallResult.Error -> {
                if (result.exception is InfraException.Server && result.exception.errorCode == APIErrorCode.INVALID_ACCESS_TOKEN) {
                    val tokenRefreshResult = withContext(ioDispatcher) {
                        authDataSource.refreshToken(TokenRefreshRequest(tokenDataSource.getRefreshToken()))
                    }
                    when (tokenRefreshResult) {
                        is CallResult.Error -> {
                            if (tokenRefreshResult.exception is InfraException.Server && result.exception.errorCode == APIErrorCode.INVALID_REFRESH_TOKEN) {
                                tokenDataSource.removeToken()
                            }
                        }

                        is CallResult.Success -> {
                            tokenDataSource.storeToken(
                                accessToken = tokenRefreshResult.data.accessToken,
                                refreshToken = tokenRefreshResult.data.refreshToken
                            )
                            tokenDataSource.setAccessToken()
                            return withContext(ioDispatcher) {
                                response
                            }
                        }
                    }
                }
                return result
            }
        }
    }
}