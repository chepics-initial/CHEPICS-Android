package com.chepics.chepics.domainmodel

import android.net.Uri
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson
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
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class CommentNavType: JsonNavType<Comment>() {
    override fun fromJsonParse(value: String): Comment {
        return Gson().fromJson(value, Comment::class.java)
    }

    override fun Comment.getJsonParse(): String {
        return Gson().toJson(this)
    }
}