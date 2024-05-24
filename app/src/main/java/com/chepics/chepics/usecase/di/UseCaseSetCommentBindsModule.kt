package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.SetCommentUseCase
import com.chepics.chepics.usecase.SetCommentUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseSetCommentBindsModule {
    @Binds
    @Singleton
    abstract fun bindSetCommentUseCase(impl: SetCommentUseCaseImpl): SetCommentUseCase
}