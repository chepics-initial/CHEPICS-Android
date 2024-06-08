package com.chepics.chepics.usecase

import android.net.Uri
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.repository.user.UserRepository
import javax.inject.Inject

interface EditProfileUseCase {
    suspend fun updateUser(
        username: String,
        fullname: String,
        bio: String?,
        imageUri: Uri?
    ): CallResult<Unit>
}

internal class EditProfileUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository
) : EditProfileUseCase {
    override suspend fun updateUser(
        username: String,
        fullname: String,
        bio: String?,
        imageUri: Uri?
    ): CallResult<Unit> {
        return userRepository.updateUser(
            username = username,
            fullname = fullname,
            bio = bio,
            imageUri = imageUri
        )
    }

}