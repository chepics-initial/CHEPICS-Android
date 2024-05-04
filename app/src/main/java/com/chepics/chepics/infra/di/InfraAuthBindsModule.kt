package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.api.auth.AuthApi
import com.chepics.chepics.infra.datasource.api.auth.AuthRemoteSource
import com.chepics.chepics.repository.auth.AuthDataSource
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
internal object InfraAuthProvidesModule {
    @Provides
    @Singleton
    fun provide(retrofit: Retrofit): AuthApi = retrofit.create()
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraAuthBindsModule {
    @Binds
    @Singleton
    abstract fun provideAuthDataSource(impl: AuthRemoteSource): AuthDataSource
}