package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.ExploreResultUseCase
import com.chepics.chepics.usecase.ExploreResultUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseExploreResultBindsModule {
    @Binds
    @Singleton
    abstract fun bindExploreResultUseCase(impl: ExploreResultUseCaseImpl): ExploreResultUseCase
}