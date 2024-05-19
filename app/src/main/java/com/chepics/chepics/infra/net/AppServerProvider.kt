package com.chepics.chepics.infra.net

import android.net.Uri
import com.chepics.chepics.repository.net.AppServerDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppServerProvider @Inject constructor(
    private val appHostProvider: AppHostProvider
) : AppServerDataSource {

    override fun provideAsUrl(): String {
        return provideAsBuilder().build().toString()
    }

    override fun provideAsBuilder(): Uri.Builder {
        return Uri.Builder()
            .scheme(HTTP)
            .authority(appHostProvider.provide().hostName)
    }

    companion object {
        private const val HTTP = "http"
    }
}