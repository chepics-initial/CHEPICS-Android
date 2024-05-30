package com.chepics.chepics.domainmodel

import android.net.Uri
import androidx.annotation.Keep
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class PickSet(
    @SerialName("set_id")
    val id: String,
    @SerialName("set_name")
    val name: String,
    @SerialName("user_pick_count")
    val votes: Int,
    @SerialName("comment_count")
    val commentCount: Int,
    @SerialName("user_pick_rate")
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
