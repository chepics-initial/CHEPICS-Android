package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.TopicTopUseCase
import com.chepics.chepics.usecase.TopicTopUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseTopicTopBindsModule {
    @Binds
    @Singleton
    abstract fun bindTopicTopUseCase(impl: TopicTopUseCaseImpl): TopicTopUseCase
}