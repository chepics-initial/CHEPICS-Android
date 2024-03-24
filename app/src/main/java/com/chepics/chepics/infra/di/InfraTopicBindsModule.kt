package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.api.topic.TopicRemoteSource
import com.chepics.chepics.repository.topic.TopicDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraTopicBindsModule {
    @Binds
    @Singleton
    abstract fun provideTopicDataSource(impl: TopicRemoteSource): TopicDataSource
}