package com.chepics.chepics.infra.di

import com.chepics.chepics.infra.net.AppServerProvider
import com.chepics.chepics.infra.net.RequestHeaderInterceptor
import com.chepics.chepics.repository.net.AppServerDataSource
import com.chepics.chepics.repository.net.RequestHeaderRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.ElementsIntoSet
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
internal object InfraNetModule {
    private const val DEFAULT_TIME_OUT_SECONDS = 30

    @Provides
    @ElementsIntoSet
    @OkHttpNetworkInterceptor
    fun provideEmptyNetworkInterceptorSet(): Set<Interceptor> = emptySet()

    @Provides
    @IntoSet
    @OkHttpInterceptor
    fun provideRequestHeaderInterceptor(repository: RequestHeaderRepository): Interceptor =
        RequestHeaderInterceptor(repository)

    @Provides
    @Singleton
    fun provideRetrofit(
        @OkHttpInterceptor interceptors: Set<@JvmSuppressWildcards Interceptor>,
        @OkHttpNetworkInterceptor networkInterceptors: Set<@JvmSuppressWildcards Interceptor>,
        appServerDataSource: AppServerDataSource,
    ): Retrofit {
        val contentType = "application/json".toMediaType()

        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .apply {
                readTimeout(DEFAULT_TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
                connectTimeout(DEFAULT_TIME_OUT_SECONDS.toLong(), TimeUnit.SECONDS)
                retryOnConnectionFailure(false)
                interceptors().addAll(interceptors)
                networkInterceptors().addAll(networkInterceptors)
            }

        val json = Json {
            // 厳密なJSON仕様に従ってなくても許容
            isLenient = true
            // 未定義のkeyがあっても許容
            ignoreUnknownKeys = true
            // enumで未定義の値があっても、デフォルト値が設定されている場合エラーにならずその値でパースを成功させる。
            // Non-nullableなプロパティに対して、Jsonでnullが返却された場合、デフォルト値があればその値でパースを成功させる。
            coerceInputValues = true
            explicitNulls = false
        }

        return Retrofit.Builder()
            .baseUrl(appServerDataSource.provideAsUrl())
            .addConverterFactory(
//                json.asConverterFactory(contentType)
                GsonConverterFactory.create()
            )
            .client(okHttpClientBuilder.build())
            .build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class InfraNetBindsModule {
    @Singleton
    @Binds
    abstract fun bindAppServerProvider(impl: AppServerProvider): AppServerDataSource
}