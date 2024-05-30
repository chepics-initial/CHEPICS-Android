package com.chepics.chepics.repository.set

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.CreateSetRequest
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.PickSetRequest
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthDataSource
import com.chepics.chepics.repository.token.TokenDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface SetRepository {
    suspend fun fetchSets(topicId: String): CallResult<List<PickSet>>
    suspend fun createSet(body: CreateSetRequest): CallResult<Unit>
    suspend fun pickSet(body: PickSetRequest): CallResult<PickSet>
    suspend fun fetchSet(setId: String): CallResult<PickSet>
}

internal class SetRepositoryImpl @Inject constructor(
    private val setDataSource: SetDataSource,
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SetRepository {
    override suspend fun fetchSets(topicId: String): CallResult<List<PickSet>> {
        return handleResponse(setDataSource.fetchSets(topicId))
    }

    override suspend fun createSet(body: CreateSetRequest): CallResult<Unit> {
        return handleResponse(setDataSource.createSet(body))
    }

    override suspend fun pickSet(body: PickSetRequest): CallResult<PickSet> {
        return handleResponse(setDataSource.pickSet(body))
    }

    override suspend fun fetchSet(setId: String): CallResult<PickSet> {
        return handleResponse(setDataSource.fetchSet(setId))
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