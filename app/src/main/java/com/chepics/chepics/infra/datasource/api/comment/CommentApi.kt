package com.chepics.chepics.infra.datasource.api.comment

import com.chepics.chepics.domainmodel.Comment
import com.chepics.chepics.domainmodel.CreateCommentResponse
import com.chepics.chepics.domainmodel.Items
import com.chepics.chepics.domainmodel.LikeRequest
import com.chepics.chepics.domainmodel.LikeResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @POST("v1/chepics/comment/like")
    suspend fun like(@Body request: LikeRequest): Response<LikeResponse>

    @Multipart
    @POST("v1/chepics/comment")
    suspend fun createComment(
        @Part("parent_id") parentId: RequestBody?,
        @Part("topic_id") topicId: RequestBody?,
        @Part("set_id") setId: RequestBody?,
        @Part("comment") comment: RequestBody,
        @Part("comment_link") link: RequestBody?,
        @Part("to_user_ids[0]") replyFor: RequestBody?,
        @Part("comment_images[0][seq_no]") imageNumber1: RequestBody?,
        @Part("comment_images[1][seq_no]") imageNumber2: RequestBody?,
        @Part("comment_images[2][seq_no]") imageNumber3: RequestBody?,
        @Part("comment_images[3][seq_no]") imageNumber4: RequestBody?,
        @Part image1: MultipartBody.Part?,
        @Part image2: MultipartBody.Part?,
        @Part image3: MultipartBody.Part?,
        @Part image4: MultipartBody.Part?
    ): Response<CreateCommentResponse>
}