package com.chepics.chepics.domainmodel

import java.time.LocalDateTime

data class Comment(
    val id: String,
    val parentId: String,
    val topicId: String,
    val setId: String,
    val comment: String,
    val link: String?,
    val images: List<CommentImage>?,
    val votes: Int,
    val user: User,
    val registerTime: LocalDateTime,
    val updateTime: LocalDateTime
)
