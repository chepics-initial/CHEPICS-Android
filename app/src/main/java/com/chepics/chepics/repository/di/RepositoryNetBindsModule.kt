package com.chepics.chepics.repository.di

import com.chepics.chepics.repository.net.RequestHeaderRepository
import com.chepics.chepics.repository.net.RequestHeaderRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositoryNetBindsModule {

    @Binds
    @Singleton
    abstract fun bindRequestHeaderRepository(impl: RequestHeaderRepositoryImpl): RequestHeaderRepository
}