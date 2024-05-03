package com.chepics.chepics.repository.di

import com.chepics.chepics.repository.comment.CommentRepository
import com.chepics.chepics.repository.comment.CommentRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryCommentBindsModule {
    @Binds
    @Singleton
    abstract fun bindCommentRepository(impl: CommentRepositoryImpl): CommentRepository
}