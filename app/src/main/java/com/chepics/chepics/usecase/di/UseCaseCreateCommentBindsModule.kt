package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.CreateCommentUseCase
import com.chepics.chepics.usecase.CreateCommentUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseCreateCommentBindsModule {
    @Binds
    @Singleton
    abstract fun bindCreateCommentUseCase(impl: CreateCommentUseCaseImpl): CreateCommentUseCase
}