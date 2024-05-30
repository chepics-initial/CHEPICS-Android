package com.chepics.chepics.infra.datasource.api.auth

import com.chepics.chepics.domainmodel.AuthResponse
import com.chepics.chepics.domainmodel.CheckCodeRequest
import com.chepics.chepics.domainmodel.CreateCode
import com.chepics.chepics.domainmodel.CreateUserRequest
import com.chepics.chepics.domainmodel.LoginRequest
import com.chepics.chepics.domainmodel.TokenRefreshRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("v1/chepics/auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @POST("v1/chepics/auth/email-confirm-code")
    suspend fun createCode(@Body request: CreateCode): Response<CreateCode>

    @POST("v1/chepics/auth/email-confirm-code/check")
    suspend fun checkCode(@Body request: CheckCodeRequest): Response<CreateCode>

    @POST("v1/chepics/auth/user")
    suspend fun createUser(@Body request: CreateUserRequest): Response<AuthResponse>

    @POST("v1/chepics/auth/token/refresh")
    suspend fun refreshToken(@Body request: TokenRefreshRequest): Response<AuthResponse>
}