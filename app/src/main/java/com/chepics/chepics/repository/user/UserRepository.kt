package com.chepics.chepics.repository.user

import android.net.Uri
import com.chepics.chepics.common.di.IoDispatcher
import com.chepics.chepics.domainmodel.APIErrorCode
import com.chepics.chepics.domainmodel.FollowRequest
import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.UserData
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthDataSource
import com.chepics.chepics.repository.token.TokenDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface UserRepository {
    fun getUserId(): String
    suspend fun fetchUser(userId: String): CallResult<User>
    suspend fun follow(request: FollowRequest): CallResult<Boolean>
    suspend fun updateUser(
        username: String,
        fullname: String,
        bio: String?,
        imageUri: Uri?
    ): CallResult<Unit>

    fun getUserData(): UserData?
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
        return when (val result = handleResponse {
            userDataSource.fetchUser(userId)
        }) {
            is CallResult.Success -> {
                if (userId == getUserId()) {
                    userStoreDataSource.storeUserData(
                        UserData(
                            username = result.data.username,
                            fullname = result.data.fullname,
                            bio = result.data.bio,
                            imageUrl = result.data.profileImageUrl
                        )
                    )
                }
                result
            }

            is CallResult.Error -> result
        }
    }

    override suspend fun follow(request: FollowRequest): CallResult<Boolean> {
        return handleResponse {
            userDataSource.follow(request)
        }
    }

    override suspend fun updateUser(
        username: String,
        fullname: String,
        bio: String?,
        imageUri: Uri?
    ): CallResult<Unit> {
        when (
            val result = handleResponse {
                userDataSource.updateUser(
                    username = username,
                    fullname = fullname,
                    bio = bio,
                    imageUri = imageUri
                )
            }
        ) {
            is CallResult.Success -> {
                userStoreDataSource.storeUserData(
                    UserData(
                        username = username,
                        fullname = fullname,
                        bio = bio,
                        imageUrl = imageUri.toString()
                    )
                )
                return result
            }

            is CallResult.Error -> return result
        }
    }

    override fun getUserData(): UserData? {
        return userStoreDataSource.getUserData()
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