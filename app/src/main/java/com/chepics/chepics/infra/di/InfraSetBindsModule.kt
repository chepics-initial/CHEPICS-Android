package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.api.set.SetApi
import com.chepics.chepics.infra.datasource.api.set.SetRemoteSource
import com.chepics.chepics.repository.set.SetDataSource
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
internal object InfraSetProvidesModule {
    @Provides
    @Singleton
    fun provide(retrofit: Retrofit): SetApi = retrofit.create()
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraSetBindsModule {
    @Binds
    @Singleton
    abstract fun provideSetDataSource(impl: SetRemoteSource): SetDataSource
}