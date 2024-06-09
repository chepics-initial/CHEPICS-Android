package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.auth.CompleteRegistrationUseCase
import com.chepics.chepics.usecase.auth.CompleteRegistrationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseCompleteRegistrationBindsModule {
    @Binds
    @Singleton
    abstract fun bindCompleteRegistrationUseCase(impl: CompleteRegistrationUseCaseImpl): CompleteRegistrationUseCase
}