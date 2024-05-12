package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.auth.OneTimeCodeUseCase
import com.chepics.chepics.usecase.auth.OneTimeCodeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseOneTimeCodeBindsModule {
    @Binds
    @Singleton
    abstract fun bindOneTimeCodeUseCase(impl: OneTimeCodeUseCaseImpl): OneTimeCodeUseCase
}