package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.datasource.local.token.TokenLocalSource
import com.chepics.chepics.repository.token.TokenDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraTokenBindsModule {
    @Binds
    @Singleton
    abstract fun provideTokenDataSource(impl: TokenLocalSource): TokenDataSource
}