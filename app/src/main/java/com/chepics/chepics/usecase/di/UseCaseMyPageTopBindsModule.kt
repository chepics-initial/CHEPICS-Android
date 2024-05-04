package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.MyPageTopUseCase
import com.chepics.chepics.usecase.MyPageTopUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseMyPageTopBindsModule {
    @Binds
    @Singleton
    abstract fun bindMyPageTopUseCase(impl: MyPageTopUseCaseImpl): MyPageTopUseCase
}