package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.api.comment.CommentApi
import com.chepics.chepics.infra.datasource.api.comment.CommentRemoteSource
import com.chepics.chepics.repository.comment.CommentDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object InfraCommentProvidesModule {
    @Provides
    @Singleton
    fun provide(retrofit: Retrofit): CommentApi = retrofit.create()
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraCommentBindsModule {
    @Binds
    @Singleton
    abstract fun provideCommentDataSource(impl: CommentRemoteSource): CommentDataSource
}