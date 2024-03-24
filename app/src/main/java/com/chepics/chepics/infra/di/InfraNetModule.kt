package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.api.topic.TopicApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object InfraNetModule {
//    @Provides
//    @IntoSet
//    @OkHttpInter
//    fun provideRequestHeaderInterceptor(repository: RequestHeaderRepository): Interceptor = RequestHeaderInterceptor(repository)


    // TODO: - 修正絶対必要
    @Singleton
    @Provides
    fun provideTopicApi(): TopicApi {
        return Retrofit.Builder()
            .baseUrl("https://www.googleapis.com/books/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TopicApi::class.java)
    }
}