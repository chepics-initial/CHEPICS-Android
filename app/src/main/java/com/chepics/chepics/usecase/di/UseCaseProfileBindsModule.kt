package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.ProfileUseCase
import com.chepics.chepics.usecase.ProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseProfileBindsModule {
    @Binds
    @Singleton
    abstract fun bindProfileUseCase(impl: ProfileUseCaseImpl): ProfileUseCase
}