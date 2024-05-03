package com.chepics.chepics.infra.datasource.api.topic

import com.chepics.chepics.domainmodel.Topic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TopicApi {
    @GET("v1/chepics/topics")
    suspend fun fetchFavoriteTopics(@Query("offset") offset: Int?): Response<List<Topic>>
}