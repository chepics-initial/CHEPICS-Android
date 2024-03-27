package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.local.header.RequestHeaderCacheSource
import com.chepics.chepics.repository.net.RequestHeaderDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object InfraHeaderBindsModule {
    @Provides
    @Singleton
    fun provideRequestHeaderDataSource(): RequestHeaderDataSource = RequestHeaderCacheSource
}