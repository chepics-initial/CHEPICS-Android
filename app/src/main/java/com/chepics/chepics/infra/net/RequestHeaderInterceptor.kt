package com.chepics.chepics.infra.net

import com.chepics.chepics.repository.net.RequestHeaderRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestHeaderInterceptor @Inject constructor(
    private val repository: RequestHeaderRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()
            // 原因が不明だが下記で修正できるので追加 https://stackoverflow.com/questions/45838774/java-io-ioexception-unexpected-end-of-stream-on-connection-in-android
            .addHeader("Connection", "close")

        repository.getHeaders().forEach { (k, v) ->
            builder.addHeader(k, v)
        }

        val newRequest = builder.build()
        return chain.proceed(newRequest)
    }
}