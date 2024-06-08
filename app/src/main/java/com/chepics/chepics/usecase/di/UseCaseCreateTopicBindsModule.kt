package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.CreateTopicUseCase
import com.chepics.chepics.usecase.CreateTopicUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseCreateTopicBindsModule {
    @Binds
    @Singleton
    abstract fun bindCreateTopicUseCase(impl: CreateTopicUseCaseImpl): CreateTopicUseCase
}