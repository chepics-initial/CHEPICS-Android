package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.SetCommentDetailUseCase
import com.chepics.chepics.usecase.SetCommentDetailUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseSetCommentDetailBindsModule {
    @Binds
    @Singleton
    abstract fun bindSetCommentDetailUseCase(impl: SetCommentDetailUseCaseImpl): SetCommentDetailUseCase
}