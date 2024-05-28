package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.auth.PasswordRegistrationUseCase
import com.chepics.chepics.usecase.auth.PasswordRegistrationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCasePasswordRegistrationBindsModule {
    @Binds
    @Singleton
    abstract fun bindPasswordUseCase(impl: PasswordRegistrationUseCaseImpl): PasswordRegistrationUseCase
}