package com.chepics.chepics.usecase.auth

import android.net.Uri
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.auth.AuthRepository
import com.chepics.chepics.repository.user.UserRepository
import java.lang.Exception
import javax.inject.Inject

interface IconRegistrationUseCase {
    suspend fun registerIcon(uri: Uri): CallResult<Unit>
    suspend fun skip()
}

internal class IconRegistrationUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : IconRegistrationUseCase {
    override suspend fun registerIcon(uri: Uri): CallResult<Unit> {
        userRepository.getUserData()?.let { userData ->
            when (val result = userRepository.updateUser(
                username = userData.username,
                fullname = userData.fullname,
                bio = userData.bio,
                imageUri = uri
            )) {
                is CallResult.Success -> {
                    authRepository.skip()
                    return result
                }

                is CallResult.Error -> return result
            }
        }

        return CallResult.Error(exception = Exception())
    }

    override suspend fun skip() {
        authRepository.skip()
    }
}