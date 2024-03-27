package com.chepics.chepics.repository.net

import android.net.Uri

interface AppServerDataSource {
    fun provideAsUrl(): String
    fun provideAsBuilder(): Uri.Builder
}