package com.chepics.chepics.domainmodel

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
)