package com.chepics.chepics.infra.datasource.api.comment

import com.chepics.chepics.domainmodel.Topic
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentApi {
    @GET("v1/chepics/following-users/comments")
    suspend fun fetchFavoriteTopics(@Query("offset") offset: Int?): Response<List<Topic>>
}