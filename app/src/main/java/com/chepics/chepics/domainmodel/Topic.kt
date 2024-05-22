package com.chepics.chepics.domainmodel

import android.net.Uri
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson
import java.time.LocalDateTime

data class Topic(
    val id: String,
    val title: String,
    val link: String?,
    val description: String?,
    val images: List<TopicImage>?,
    val user: User,
    val votes: Int,
    val set: List<PickSet>?,
    val registerTime: LocalDateTime,
    val updateTime: LocalDateTime
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class TopicNavType : JsonNavType<Topic>() {
    override fun fromJsonParse(value: String): Topic {
        return Gson().fromJson(value, Topic::class.java)
    }

    override fun Topic.getJsonParse(): String {
        return Gson().toJson(this)
    }

}