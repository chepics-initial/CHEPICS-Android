package com.chepics.chepics.domainmodel

import com.google.gson.annotations.SerializedName


data class TopicImage(
    @SerializedName("topic_id")
    val topicId: String?,
    @SerializedName("seq_no")
    val number: Int,
    @SerializedName("image_url")
    val url: String
)