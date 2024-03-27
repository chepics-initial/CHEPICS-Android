package com.chepics.chepics.infra.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpInterceptor

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OkHttpNetworkInterceptor