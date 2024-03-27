package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.api.topic.TopicApi
import com.chepics.chepics.infra.datasource.api.topic.TopicRemoteSource
import com.chepics.chepics.repository.topic.TopicDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object InfraTopicProvidesModule {
    @Provides
    @Singleton
    fun provide(retrofit: Retrofit): TopicApi = retrofit.create()
}

// よくわからんけど、abstruct funを使いたいからprovideTopicDataSourceはabstract classにしないといけないみたい。
// そうじゃない場合は極力objectを使えということかも

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraTopicBindsModule {
    @Binds
    @Singleton
    abstract fun provideTopicDataSource(impl: TopicRemoteSource): TopicDataSource
}