package com.chepics.chepics.domainmodel

import android.net.Uri
import androidx.annotation.Keep
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.chepics.chepics.domainmodel.serializer.LocalDateTimeSerializer
import com.google.gson.Gson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Keep
@Serializable
data class Topic(
    @SerialName("topic_id")
    val id: String,
    @SerialName("topic_name")
    val title: String,
    @SerialName("topic_link")
    val link: String?,
    @SerialName("topic_description")
    val description: String?,
    @SerialName("topic_images")
    val images: List<TopicImage>?,
    @SerialName("create_user")
    val user: User,
    @SerialName("user_pick_count")
    val votes: Int,
    val set: List<PickSet>?,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("register_time")
    val registerTime: LocalDateTime
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