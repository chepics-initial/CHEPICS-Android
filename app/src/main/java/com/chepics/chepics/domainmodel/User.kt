package com.chepics.chepics.domainmodel

import android.net.Uri
import androidx.annotation.Keep
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class User(
    @SerialName("user_id")
    val id: String,
    @SerialName("user_name")
    val username: String,
    @SerialName("display_name")
    val fullname: String,
    @SerialName("user_image_url")
    val profileImageUrl: String?,
    val bio: String?,
    @SerialName("is_following")
    val isFollowing: Boolean?,
    @SerialName("is_followed")
    val isFollowed: Boolean?,
    @SerialName("following_user_count")
    val following: Int?,
    @SerialName("followed_user_count")
    val followers: Int?
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class UserNavType : JsonNavType<User>() {
    override fun fromJsonParse(value: String): User {
        return Gson().fromJson(value, User::class.java)
    }

    override fun User.getJsonParse(): String {
        return Gson().toJson(this)
    }
}