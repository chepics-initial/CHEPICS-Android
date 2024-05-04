package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.api.search.SearchApi
import com.chepics.chepics.infra.datasource.api.search.SearchRemoteSource
import com.chepics.chepics.repository.search.SearchDataSource
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
internal object InfraSearchProvidesModule {
    @Provides
    @Singleton
    fun provide(retrofit: Retrofit): SearchApi = retrofit.create()
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraSearchBindsModule {
    @Binds
    @Singleton
    abstract fun provideSearchDataSource(impl: SearchRemoteSource): SearchDataSource
}