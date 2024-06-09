package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.auth.IconRegistrationUseCase
import com.chepics.chepics.usecase.auth.IconRegistrationUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseIconRegistrationBindsModule {
    @Binds
    @Singleton
    abstract fun bindIconRegistrationUseCase(impl: IconRegistrationUseCaseImpl): IconRegistrationUseCase
}