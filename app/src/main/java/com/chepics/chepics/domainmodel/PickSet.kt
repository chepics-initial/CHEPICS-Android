package com.chepics.chepics.domainmodel

import android.net.Uri
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson

data class PickSet(
    val id: String,
    val name: String,
    val votes: Int,
    val commentCount: Int
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class SetNavType : JsonNavType<PickSet>() {
    override fun fromJsonParse(value: String): PickSet {
        return Gson().fromJson(value, PickSet::class.java)
    }

    override fun PickSet.getJsonParse(): String {
        return Gson().toJson(this)
    }

}
