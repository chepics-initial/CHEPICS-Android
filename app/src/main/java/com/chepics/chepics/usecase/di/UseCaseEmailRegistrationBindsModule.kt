package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.auth.EmailRegistrationUseCase
import com.chepics.chepics.usecase.auth.EmailRegistrationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseEmailRegistrationBindsModule {
    @Binds
    @Singleton
    abstract fun bindEmailRegistrationUseCase(impl: EmailRegistrationUseCaseImpl): EmailRegistrationUseCase
}