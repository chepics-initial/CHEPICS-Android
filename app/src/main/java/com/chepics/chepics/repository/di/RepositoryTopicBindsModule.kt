package com.chepics.chepics.repository.di

import com.chepics.chepics.repository.topic.TopicRepository
import com.chepics.chepics.repository.topic.TopicRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryTopicBindsModule {
    @Binds
    @Singleton
    abstract fun bindTopicRepository(impl: TopicRepositoryImpl): TopicRepository
}