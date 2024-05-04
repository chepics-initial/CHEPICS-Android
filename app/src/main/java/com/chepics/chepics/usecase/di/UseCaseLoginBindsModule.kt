package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.auth.LoginUseCase
import com.chepics.chepics.usecase.auth.LoginUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseLoginBindsModule {
    @Binds
    @Singleton
    abstract fun bindLoginUseCase(impl: LoginUseCaseImpl): LoginUseCase
}