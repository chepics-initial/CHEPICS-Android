package com.chepics.chepics.repository.user

import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthDataSource
import com.chepics.chepics.repository.token.TokenDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserRepository {
    fun getUserId(): String
    suspend fun fetchUser(userId: String): CallResult<User>
}

internal class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val userStoreDataSource: UserStoreDataSource,
    private val authDataSource: AuthDataSource,
    private val tokenDataSource: TokenDataSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    override fun getUserId(): String {
        return userStoreDataSource.getUserId()
    }

    override suspend fun fetchUser(userId: String): CallResult<User> {
        return handleResponse(userDataSource.fetchUser(userId))
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