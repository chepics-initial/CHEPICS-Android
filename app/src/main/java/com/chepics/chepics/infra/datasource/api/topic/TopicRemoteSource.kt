package com.chepics.chepics.infra.datasource.api.topic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.infra.datasource.api.safeApiCall
import com.chepics.chepics.repository.topic.TopicDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.InputStream
import javax.inject.Inject

class TopicRemoteSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val api: TopicApi
) : TopicDataSource {
    override suspend fun fetchFavoriteTopics(offset: Int?): CallResult<List<Topic>> {
        return safeApiCall { api.fetchFavoriteTopics(offset) }.mapSuccess { it.items }
    }

    override suspend fun fetchUserTopics(userId: String, offset: Int?): CallResult<List<Topic>> {
        return safeApiCall {
            api.fetchUserTopics(
                userId = userId,
                offset = offset
            )
        }.mapSuccess { it.items }
    }

    override suspend fun fetchTopic(topicId: String): CallResult<Topic> {
        return safeApiCall { api.fetchTopic(topicId) }
    }

    override suspend fun createTopic(
        title: String,
        link: String?,
        description: String?,
        images: List<Uri>?
    ): CallResult<Unit> {
        val titleData = title.toRequestBody("text/plain".toMediaTypeOrNull())
        var linkData: RequestBody? = null
        link?.let {
            linkData = it.toRequestBody("text/plain".toMediaTypeOrNull())
        }
        var descriptionData: RequestBody? = null
        description?.let {
            descriptionData = it.toRequestBody("text/plain".toMediaTypeOrNull())
        }
        var image1: MultipartBody.Part? = null
        var image2: MultipartBody.Part? = null
        var image3: MultipartBody.Part? = null
        var image4: MultipartBody.Part? = null
        var imageNumber1: RequestBody? = null
        var imageNumber2: RequestBody? = null
        var imageNumber3: RequestBody? = null
        var imageNumber4: RequestBody? = null
        images?.let { imageUris ->
            imageUris.forEachIndexed { index, uri ->
                when (index) {
                    0 -> {
                        image1 = createMultipartBodyPart(uri, index)
                        imageNumber1 =
                            "${index + 1}".toRequestBody("text/plain".toMediaTypeOrNull())
                    }

                    1 -> {
                        image2 = createMultipartBodyPart(uri, index)
                        imageNumber2 =
                            "${index + 1}".toRequestBody("text/plain".toMediaTypeOrNull())
                    }

                    2 -> {
                        image3 = createMultipartBodyPart(uri, index)
                        imageNumber3 =
                            "${index + 1}".toRequestBody("text/plain".toMediaTypeOrNull())
                    }

                    3 -> {
                        image4 = createMultipartBodyPart(uri, index)
                        imageNumber4 =
                            "${index + 1}".toRequestBody("text/plain".toMediaTypeOrNull())
                    }
                }
            }
        }
        return safeApiCall {
            api.createTopic(
                title = titleData,
                link = linkData,
                description = descriptionData,
                imageNumber1 = imageNumber1,
                imageNumber2 = imageNumber2,
                imageNumber3 = imageNumber3,
                imageNumber4 = imageNumber4,
                image1 = image1,
                image2 = image2,
                image3 = image3,
                image4 = image4
            )
        }.mapSuccess { }
    }

    private fun createMultipartBodyPart(uri: Uri, index: Int): MultipartBody.Part {
        val imageStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(imageStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        val compressFormat = Bitmap.CompressFormat.JPEG
        bitmap.compress(compressFormat, 80, byteArrayOutputStream)
        return MultipartBody.Part.createFormData(
            "topic_images[$index][image_file]",
            "image$index.jpg",
            byteArrayOutputStream.toByteArray()
                .toRequestBody(
                    "image/jpeg".toMediaTypeOrNull(),
                    0,
                    byteArrayOutputStream.toByteArray().size
                )
        )
    }
}