package com.chepics.chepics.infra.net

interface AppHostProvider {
    fun provide(): AppHost
}