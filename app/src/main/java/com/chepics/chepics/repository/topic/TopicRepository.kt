package com.chepics.chepics.repository.topic

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthDataSource
import com.chepics.chepics.repository.token.TokenDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface TopicRepository {
    suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>>
    suspend fun fetchUserTopics(userId: String, offset: Int?): CallResult<List<Topic>>
    suspend fun fetchTopic(topicId: String): CallResult<Topic>
}

internal class TopicRepositoryImpl @Inject constructor(
    private val topicDataSource: TopicDataSource,
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : TopicRepository {
    override suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>> {
        return handleResponse(topicDataSource.fetchFavoriteTopics(offset))
//        return withContext(ioDispatcher) {
//            topicDataSource.fetchFavoriteTopics(offset)
//        }
    }

    override suspend fun fetchUserTopics(userId: String, offset: Int?): CallResult<List<Topic>> {
        return withContext(ioDispatcher) {
            topicDataSource.fetchUserTopics(userId = userId, offset = offset)
        }
    }

    override suspend fun fetchTopic(topicId: String): CallResult<Topic> {
        return withContext(ioDispatcher) {
            topicDataSource.fetchTopic(topicId)
        }
    }

    private suspend fun <T : Any> handleResponse(response: CallResult<T>): CallResult<T> {
        val result = withContext(ioDispatcher) {
            response
        }

        when (result) {
            is CallResult.Success -> return result
            is CallResult.Error -> {
                if (result.exception is InfraException.Server && result.exception.errorCode == APIErrorCode.INVALID_ACCESS_TOKEN) {
                    when (val tokenRefreshResult =
                        authDataSource.refreshToken(TokenRefreshRequest(tokenDataSource.getRefreshToken()))) {
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
                            return response
                        }
                    }
                }
                return result
            }
        }
    }
}