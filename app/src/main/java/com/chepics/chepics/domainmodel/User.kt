package com.chepics.chepics.domainmodel

import android.net.Uri
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val id: String,
    @SerializedName("user_name")
    val username: String,
    @SerializedName("display_name")
    val fullname: String,
    @SerializedName("user_image_url")
    val profileImageUrl: String?,
    val bio: String?,
    @SerializedName("is_following")
    val isFollowing: Boolean?,
    @SerializedName("is_followed")
    val isFollowed: Boolean?,
    @SerializedName("following_user_count")
    val following: Int?,
    @SerializedName("followed_user_count")
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