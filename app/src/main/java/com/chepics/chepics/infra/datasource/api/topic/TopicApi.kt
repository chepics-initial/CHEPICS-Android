package com.chepics.chepics.infra.datasource.api.topic

import com.chepics.chepics.domainmodel.CreateTopicResponse
import com.chepics.chepics.domainmodel.Items
import com.chepics.chepics.domainmodel.Topic
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface TopicApi {
    @GET("v1/chepics/topics/recommended")
    suspend fun fetchFavoriteTopics(@Query("offset") offset: Int?): Response<Items<Topic>>

    @GET("v1/chepics/user/topics")
    suspend fun fetchUserTopics(
        @Query("user_id") userId: String,
        @Query("offset") offset: Int?
    ): Response<Items<Topic>>

    @GET("v1/chepics/topic")
    suspend fun fetchTopic(@Query("topic_id") topicId: String): Response<Topic>

    @Multipart
    @POST("v1/chepics/topic")
    suspend fun createTopic(
        @Part("topic_name") title: RequestBody,
        @Part("topic_link") link: RequestBody?,
        @Part("topic_description") description: RequestBody?,
        @Part("topic_images[0][seq_no]") imageNumber1: RequestBody?,
        @Part("topic_images[1][seq_no]") imageNumber2: RequestBody?,
        @Part("topic_images[2][seq_no]") imageNumber3: RequestBody?,
        @Part("topic_images[3][seq_no]") imageNumber4: RequestBody?,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image4: MultipartBody.Part?
    ): Response<CreateTopicResponse>
}