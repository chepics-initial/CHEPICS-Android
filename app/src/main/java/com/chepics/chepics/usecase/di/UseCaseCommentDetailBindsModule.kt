package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.CommentDetailUseCase
import com.chepics.chepics.usecase.CommentDetailUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseCommentDetailBindsModule {
    @Binds
    @Singleton
    abstract fun bindCommentDetailUseCase(impl: CommentDetailUseCaseImpl): CommentDetailUseCase
}