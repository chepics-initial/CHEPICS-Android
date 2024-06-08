package com.chepics.chepics.usecase.di

import com.chepics.chepics.usecase.MyPageTopicListUseCase
import com.chepics.chepics.usecase.MyPageTopicListUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseMyPageTopicListBindsModule {
    @Binds
    @Singleton
    abstract fun bindMyPageTopicListUseCase(impl: MyPageTopicListUseCaseImpl): MyPageTopicListUseCase
}