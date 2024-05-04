package com.chepics.chepics.infra.datasource.api.auth

import com.chepics.chepics.domainmodel.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("v1/chepics/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<Unit>
}