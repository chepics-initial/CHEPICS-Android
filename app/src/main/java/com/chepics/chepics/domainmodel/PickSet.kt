package com.chepics.chepics.domainmodel

import android.net.Uri
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PickSet(
    @SerializedName("set_id")
    val id: String,
    @SerializedName("set_name")
    val name: String,
    @SerializedName("user_pick_count")
    val votes: Int,
    @SerializedName("comment_count")
    val commentCount: Int,
    @SerializedName("user_pick_rate")
    val rate: Double
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
