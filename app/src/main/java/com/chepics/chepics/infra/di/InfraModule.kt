package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.net.AppHostProProvider
import com.chepics.chepics.infra.net.AppHostProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class InfraModule {
    @Provides
    @Singleton
    fun provideAppHostProvider(provider: AppHostProProvider): AppHostProvider = provider
}