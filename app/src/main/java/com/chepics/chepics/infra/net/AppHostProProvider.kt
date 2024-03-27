package com.chepics.chepics.infra.net

import javax.inject.Inject

class AppHostProProvider @Inject constructor(): AppHostProvider {
    override fun provide(): AppHost {
        return AppHost.PRO
    }
}