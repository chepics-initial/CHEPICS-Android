package com.chepics.chepics.infra.datasource.api.search

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Items
import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("v1/chepics/search/topic")
    suspend fun fetchSearchedTopics(
        @Query("word") word: String,
        @Query("offset") offset: Int?
    ): Response<Items<Topic>>

    @GET("v1/chepics/search/comment")
    suspend fun fetchSearchedComments(
        @Query("word") word: String,
        @Query("offset") offset: Int?
    ): Response<Items<Comment>>

    @GET("v1/chepics/search/user")
    suspend fun fetchSearchedUsers(
        @Query("word") word: String,
        @Query("offset") offset: Int?
    ): Response<Items<User>>
}