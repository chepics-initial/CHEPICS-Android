package com.chepics.chepics.domainmodel

import android.net.Uri
import com.chepics.chepics.domainmodel.common.JsonNavType
import com.google.gson.Gson

data class User(
    val id: String,
    val username: String,
    val fullname: String,
    val profileImageUrl: String?,
    val bio: String?,
    val isFollowing: Boolean?,
    val isFollowed: Boolean?
) {
    override fun toString(): String = Uri.encode(Gson().toJson(this))
}

class UserNavType: JsonNavType<User>() {
    override fun fromJsonParse(value: String): User {
        return Gson().fromJson(value, User::class.java)
    }

    override fun User.getJsonParse(): String {
        return Gson().toJson(this)
    }
}