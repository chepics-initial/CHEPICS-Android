package com.chepics.chepics.infra.datasource.api.user

import com.chepics.chepics.domainmodel.FollowRequest
import com.chepics.chepics.domainmodel.FollowResponse
import com.chepics.chepics.domainmodel.UpdateUserResponse
import com.chepics.chepics.domainmodel.User
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface UserApi {
    @GET("v1/chepics/user")
    suspend fun fetchUser(@Query("user_id") userId: String): Response<User>

    @POST("v1/chepics/follow")
    suspend fun follow(@Body request: FollowRequest): Response<FollowResponse>

    @Multipart
    @POST("v1/chepics/user/update")
    suspend fun updateUser(
        @Part("user_name") username: RequestBody,
        @Part("display_name") fullname: RequestBody,
        @Part("bio") bio: RequestBody?,
        @Part image: MultipartBody.Part?,
        @Part("is_update_user_image") isUpdated: RequestBody
    ): Response<UpdateUserResponse>

    // TODO: - デバッグ用なのであとで削除
    @DELETE("v1/chepics/user")
    suspend fun deleteUser(@Query("user_id") userId: String): Response<UpdateUserResponse>
}