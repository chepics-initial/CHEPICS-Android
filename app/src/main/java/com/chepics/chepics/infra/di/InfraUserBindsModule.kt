package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.api.user.UserApi
import com.chepics.chepics.infra.datasource.api.user.UserRemoteSource
import com.chepics.chepics.repository.user.UserDataSource
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
internal object InfraUserProvidesModule {
    @Provides
    @Singleton
    fun provide(retrofit: Retrofit): UserApi = retrofit.create()
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraUserBindsModule {
    @Binds
    @Singleton
    abstract fun provideUserDataSource(impl: UserRemoteSource): UserDataSource
}