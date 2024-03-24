package com.chepics.chepics.mock

import com.chepics.chepics.domainmodel.Topic
import com.chepics.chepics.domainmodel.TopicImage
import com.chepics.chepics.domainmodel.User
import java.time.LocalDateTime

val mockTopicImage1 = TopicImage(
    id = "1",
    topicId = "1",
    url = "https://doremifahiroba.com/wp-content/uploads/2022/11/EP01_30-1024x576.jpg"
)

val mockUser1 = User(
    id = "1",
    username = "taro",
    fullname = "太郎",
    profileImageUrl = "https://animeanime.jp/imgs/ogp_f/303592.jpg"
)

val mockTopic1 = Topic(
    id = "1",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1
    ),
    user = mockUser1,
    votes = 134,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)