package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.TokenUseCase
import com.chepics.chepics.usecase.TokenUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseTokenBindsModule {
    @Binds
    @Singleton
    abstract fun bindTokenUseCase(impl: TokenUseCaseImpl): TokenUseCase
}