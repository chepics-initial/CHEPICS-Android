package com.chepics.chepics.domainmodel

data class User(
    val id: String,
    val username: String,
    val fullname: String,
    val profileImageUrl: String?
)
