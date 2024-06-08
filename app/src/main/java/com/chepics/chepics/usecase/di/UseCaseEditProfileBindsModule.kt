package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.EditProfileUseCase
import com.chepics.chepics.usecase.EditProfileUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseEditProfileBindsModule {
    @Binds
    @Singleton
    abstract fun bindEditProfileUseCase(impl: EditProfileUseCaseImpl): EditProfileUseCase
}