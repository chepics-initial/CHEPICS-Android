package com.chepics.chepics.repository.search

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthDataSource
import com.chepics.chepics.repository.token.TokenDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SearchRepository {
    suspend fun fetchSearchedTopics(word: String): CallResult<List<Topic>>
    suspend fun fetchSearchedComments(word: String): CallResult<List<Comment>>
    suspend fun fetchSearchedUsers(word: String): CallResult<List<User>>
}

internal class SearchRepositoryImpl @Inject constructor(
    private val searchDataSource: SearchDataSource,
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SearchRepository {
    override suspend fun fetchSearchedTopics(word: String): CallResult<List<Topic>> {
        return handleResponse {
            searchDataSource.fetchSearchedTopics(word)
        }
    }

    override suspend fun fetchSearchedComments(word: String): CallResult<List<Comment>> {
        return handleResponse {
            searchDataSource.fetchSearchedComments(word)
        }
    }

    override suspend fun fetchSearchedUsers(word: String): CallResult<List<User>> {
        return handleResponse {
            searchDataSource.fetchSearchedUsers(word)
        }
    }

    private suspend fun <T : Any> handleResponse(request: suspend () -> CallResult<T>): CallResult<T> {
        val result = withContext(ioDispatcher) {
            request()
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
                            if (tokenRefreshResult.exception is InfraException.Server && tokenRefreshResult.exception.errorCode == APIErrorCode.INVALID_REFRESH_TOKEN) {
                                tokenDataSource.removeToken()
                            }
                            return tokenRefreshResult
                        }

                        is CallResult.Success -> {
                            tokenDataSource.storeToken(
                                accessToken = tokenRefreshResult.data.accessToken,
                                refreshToken = tokenRefreshResult.data.refreshToken
                            )
                            tokenDataSource.setAccessToken()
                            delay(1000L)
                            return withContext(ioDispatcher) {
                                request()
                            }
                        }
                    }
                }
                return result
            }
        }
    }
}