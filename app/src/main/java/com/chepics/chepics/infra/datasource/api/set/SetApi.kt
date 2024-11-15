package com.chepics.chepics.infra.datasource.api.set

import com.chepics.chepics.domainmodel.CreateSetRequest
import com.chepics.chepics.domainmodel.CreateSetResponse
import com.chepics.chepics.domainmodel.Items
import com.chepics.chepics.domainmodel.MySet
import com.chepics.chepics.domainmodel.PickSet
import com.chepics.chepics.domainmodel.PickSetRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SetApi {
    @GET("v1/chepics/topic/sets")
    suspend fun fetchSets(
        @Query("topic_id") topicId: String,
        @Query("offset") offset: Int?
    ): Response<Items<PickSet>>

    @POST("v1/chepics/set")
    suspend fun createSet(@Body createSetRequest: CreateSetRequest): Response<CreateSetResponse>

    @POST("v1/chepics/pick/set")
    suspend fun pickSet(@Body pickSetRequest: PickSetRequest): Response<PickSet>

    @GET("v1/chepics/set")
    suspend fun fetchSet(@Query("set_id") setId: String): Response<PickSet>

    @GET("v1/chepics/pick/sets")
    suspend fun fetchPickedSets(@Query("offset") offset: Int?): Response<Items<MySet>>

    @GET("v1/chepics/pick/set")
    suspend fun fetchPickedSet(@Query("topic_id") topicId: String): Response<PickSet>
}