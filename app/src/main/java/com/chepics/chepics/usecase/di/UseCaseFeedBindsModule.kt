package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.feed.FeedUseCase
import com.chepics.chepics.usecase.feed.FeedUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class  UseCaseFeedBindsModule {
    @Binds
    @Singleton
    abstract fun bindFeedUseCase(impl: FeedUseCaseImpl): FeedUseCase
}