package com.chepics.chepics.repository.di

import com.chepics.chepics.repository.set.SetRepository
import com.chepics.chepics.repository.set.SetRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RepositorySetBindsModule {
    @Binds
    @Singleton
    abstract fun bindSetRepository(impl: SetRepositoryImpl): SetRepository
}