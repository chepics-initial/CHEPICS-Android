package com.chepics.chepics.infra.datasource.api.user

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.chepics.chepics.domainmodel.FollowRequest
import com.chepics.chepics.domainmodel.User
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.repository.user.UserDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class UserRemoteSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: UserApi
) : UserDataSource {
    override suspend fun fetchUser(userId: String): CallResult<User> {
        return safeApiCall { api.fetchUser(userId) }
    }

    override suspend fun follow(request: FollowRequest): CallResult<Boolean> {
        return safeApiCall { api.follow(request) }.mapSuccess { it.isFollow }
    }

    override suspend fun updateUser(
        username: String,
        fullname: String,
        bio: String?,
        imageUri: Uri?
    ): CallResult<Unit> {
        val usernameData = username.toRequestBody("text/plain".toMediaTypeOrNull())
        val fullnameData = fullname.toRequestBody("text/plain".toMediaTypeOrNull())
        var bioData: RequestBody? = null
        bio?.let {
            bioData = it.toRequestBody("text/plain".toMediaTypeOrNull())
        }
        var image: MultipartBody.Part? = null
        imageUri?.let {
            val imageStream: InputStream? = context.contentResolver.openInputStream(it)
            val bitmap = BitmapFactory.decodeStream(imageStream)
            val byteArrayOutputStream = ByteArrayOutputStream()
            val compressFormat = Bitmap.CompressFormat.JPEG
            bitmap.compress(compressFormat, 80, byteArrayOutputStream)
            image = MultipartBody.Part.createFormData(
                "user_image",
                "image.jpg",
                byteArrayOutputStream.toByteArray()
                    .toRequestBody(
                        "image/jpeg".toMediaTypeOrNull(),
                        0,
                        byteArrayOutputStream.toByteArray().size
                    )
            )
        }
        val isUpdated =
            (if (image != null) "true" else "false").toRequestBody("text/plain".toMediaTypeOrNull())
        return safeApiCall {
            api.updateUser(
                username = usernameData,
                fullname = fullnameData,
                bio = bioData,
                image = image,
                isUpdated = isUpdated
            )
        }.mapSuccess { }
    }

    // TODO: - デバッグ用なのであとで削除
    override suspend fun deleteUser(userId: String): CallResult<Unit> {
        return safeApiCall { api.deleteUser(userId) }.mapSuccess { }
    }
}