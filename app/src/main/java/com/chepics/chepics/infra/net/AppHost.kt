package com.chepics.chepics.infra.net

enum class AppHost(val hostName: String) {
    PRO("chepics.com");

    companion object {
        operator fun get(index: Int): AppHost {
            require(index in values().indices) {
                "the index specified is out of the range. $index"
            }
            return values()[index]
        }
    }
}