package com.chepics.chepics.infra.datasource.api.topic

import com.chepics.chepics.domainmodel.Topic
import retrofit2.Response
import retrofit2.http.GET
import javax.inject.Singleton

interface TopicApi {
    // TODO: - 仮置きなので修正必要
    @GET
    suspend fun fetchFavoriteTopics(): Response<List<Topic>>
}