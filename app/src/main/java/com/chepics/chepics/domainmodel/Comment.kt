package com.chepics.chepics.domainmodel

import android.net.Uri
import androidx.annotation.Keep
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Keep
@Serializable
data class Comment(
    @SerialName("comment_id")
    val id: String,
    @SerialName("parent_id")
    val parentId: String?,
    @SerialName("topic_id")
    val topicId: String,
    @SerialName("set_id")
    val setId: String,
    @SerialName("topic_name")
    val topic: String,
    val comment: String,
    @SerialName("comment_link")
    val link: String?,
    @SerialName("comment_image")
    val images: List<CommentImage>?,
    @SerialName("comment_like_count")
    val votes: Int,
    @SerialName("has_user_liked_comment")
    val isLiked: Boolean,
    @SerialName("create_user")
    val user: User,
//    val registerTime: LocalDateTime
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class CommentNavType : JsonNavType<Comment>() {
    override fun fromJsonParse(value: String): Comment {
        return Gson().fromJson(value, Comment::class.java)
    }

    override fun Comment.getJsonParse(): String {
        return Gson().toJson(this)
    }
}