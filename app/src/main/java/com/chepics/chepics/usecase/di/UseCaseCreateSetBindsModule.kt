package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.CreateSetUseCase
import com.chepics.chepics.usecase.CreateSetUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseCreateSetBindsModule {
    @Binds
    @Singleton
    abstract fun bindCreateSetUseCase(impl: CreateSetUseCaseImpl): CreateSetUseCase
}