package com.chepics.chepics.infra.datasource.api.comment

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.Items
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CommentApi {
    @GET("v1/chepics/users/following/comments")
    suspend fun fetchFavoriteTopics(@Query("offset") offset: Int?): Response<Items<Comment>>

    @GET("v1/chepics/user/comments")
    suspend fun fetchUserComments(
        @Query("user_id") userId: String,
        @Query("offset") offset: Int?
    ): Response<Items<Comment>>

    @GET("v1/chepics/set/comments")
    suspend fun fetchSetComments(
        @Query("set_id") setId: String,
        @Query("offset") offset: Int?
    ): Response<Items<Comment>>

    @GET("v1/chepics/comments/children")
    suspend fun fetchReplies(
        @Query("comment_id") commentId: String,
        @Query("offset") offset: Int?
    ): Response<Items<Comment>>

    @GET("v1/chepics/comment")
    suspend fun fetchComment(@Query("comment_id") commentId: String): Response<Comment>
}