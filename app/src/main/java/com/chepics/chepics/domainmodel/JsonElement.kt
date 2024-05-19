package com.chepics.chepics.domainmodel

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

val JsonElement?.contentOrNull: String?
    get() {
        return this?.let { jsonPrimitive.contentOrNull }
    }