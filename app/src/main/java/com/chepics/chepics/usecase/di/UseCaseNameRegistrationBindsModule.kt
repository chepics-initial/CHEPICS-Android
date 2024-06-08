package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.auth.NameRegistrationUseCase
import com.chepics.chepics.usecase.auth.NameRegistrationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseNameRegistrationBindsModule {
    @Binds
    @Singleton
    abstract fun bindNameRegistrationUseCase(impl: NameRegistrationUseCaseImpl): NameRegistrationUseCase
}