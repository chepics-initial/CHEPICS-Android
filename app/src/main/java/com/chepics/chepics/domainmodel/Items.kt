package com.chepics.chepics.domainmodel

import kotlinx.serialization.Serializable

@Serializable
data class Items<T>(val items: List<T>)