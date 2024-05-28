package com.chepics.chepics.infra.datasource.api.topic

import com.chepics.chepics.domainmodel.Items
import com.chepics.chepics.domainmodel.Topic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TopicApi {
    @GET("v1/chepics/topics/recommended")
    suspend fun fetchFavoriteTopics(@Query("offset") offset: Int?): Response<Items<Topic>>

    @GET("v1/chepics/user/topics")
    suspend fun fetchUserTopics(
        @Query("user_id") userId: String,
        @Query("offset") offset: Int?
    ): Response<List<Topic>>

    @GET("v1/chepics/topic")
    suspend fun fetchTopic(@Query("topic_id") topicId: String): Response<Topic>
}