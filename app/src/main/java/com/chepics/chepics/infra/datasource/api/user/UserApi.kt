package com.chepics.chepics.infra.datasource.api.user

import com.chepics.chepics.domainmodel.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET("v1/chepics/user")
    suspend fun fetchFavoriteTopics(@Query("user_id") userId: String): Response<User>
}