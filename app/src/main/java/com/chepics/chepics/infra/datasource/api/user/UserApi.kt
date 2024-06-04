package com.chepics.chepics.infra.datasource.api.user

import com.chepics.chepics.domainmodel.FollowRequest
import com.chepics.chepics.domainmodel.FollowResponse
import com.chepics.chepics.domainmodel.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @GET("v1/chepics/user")
    suspend fun fetchUser(@Query("user_id") userId: String): Response<User>

    @POST("v1/chepics/follow")
    suspend fun follow(@Body request: FollowRequest): Response<FollowResponse>
}