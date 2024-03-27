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

val mockTopicImage2 = TopicImage(
    id = "2",
    topicId = "1",
    url = "https://realsound.jp/wp-content/uploads/2023/01/20230121-gudetama-07.jpg"
)

val mockTopicImage3 = TopicImage(
    id = "3",
    topicId = "1",
    url = "https://eiga.k-img.com/images/anime/news/117485/photo/46fcf777bd7b0902/640.jpg?1669974887"
)

val mockTopicImage4 = TopicImage(
    id = "4",
    topicId = "1",
    url = "https://netofuli.com/wp-content/uploads/2022/12/%E3%82%B9%E3%82%AF%E3%83%AA%E3%83%BC%E3%83%B3%E3%82%B7%E3%83%A7%E3%83%83%E3%83%88-2022-12-17-18.40.30.jpg"
)

val mockUser1 = User(
    id = "1",
    username = "taro",
    fullname = "太郎",
    profileImageUrl = "https://animeanime.jp/imgs/ogp_f/303592.jpg"
)

val mockUser2 = User(
    id = "2",
    username = "Sakura",
    fullname = "さくら",
    profileImageUrl = "https://d1uzk9o9cg136f.cloudfront.net/f/16783489/rc/2022/10/25/b86bd6fa3f0fc8c9cb3aaa5eb26bfd60066a598e.jpg"
)

val mockUser3 = User(
    id = "3",
    username = "Yuta",
    fullname = "ゆうた",
    profileImageUrl = "https://static.gltjp.com/glt/prd/data/article/21000/20584/20240112_113107_c7298818_w1920.jpg"
)

val mockUser4 = User(
    id = "4",
    username = "Akari",
    fullname = "あかり",
    profileImageUrl = "https://img.freepik.com/free-photo/fuji-mountain-and-kawaguchiko-lake-in-morning-autumn-seasons-fuji-mountain-at-yamanachi-in-japan_335224-102.jpg?size=626&ext=jpg&ga=GA1.1.967060102.1710806400&semt=ais"
)

val mockUser5 = User(
    id = "5",
    username = "Haruto",
    fullname = "はると",
    profileImageUrl = "https://fujifilmsquare.jp/assets/img/column/column_39_01.jpg"
)

val mockUser6 = User(
    id = "6",
    username = "Mizuki",
    fullname = "みずき",
    profileImageUrl = "https://tabiiro.jp/auto_sysnc/images/article/2738/share_images1671604951.jpg"
)

val mockUser7 = User(
    id = "7",
    username = "Rio",
    fullname = "りお",
    profileImageUrl = "https://img01.jalannews.jp/img/2023/06/202307_kanto_1_114-670x443.jpg"
)

val mockUser8 = User(
    id = "8",
    username = "Riku",
    fullname = "りく",
    profileImageUrl = "https://discoverjapan-web.com/wp-content/uploads/2020/06/4ffe6b26cd5640fa352a979cc7a96dd7.jpg"
)

val mockUser9 = User(
    id = "9",
    username = "Airi",
    fullname = "あいり",
    profileImageUrl = "https://prtimes.jp/i/7916/471/resize/d7916-471-835089-22.jpg"
)

val mockUser10 = User(
    id = "10",
    username = "Shunsuke",
    fullname = "しゅんすけ",
    profileImageUrl = "https://travel.rakuten.co.jp/mytrip/sites/mytrip/files/styles/main_image/public/migration_article_images/amazing/amazingviews-kanto-key.jpg?itok=F7xs4yDv"
)

val mockUser11 = User(
    id = "11",
    username = "Rena",
    fullname = "れな",
    profileImageUrl = "https://fs.tour.ne.jp/index.php/file_manage/view/?contents_code=curation&file_name=814/27186/98ca4c60e2fcc431d31fdc8fd05c7bc3.jpg&w=1200"
)

val mockUser12 = User(
    id = "12",
    username = "Kaito",
    fullname = "かいと",
    profileImageUrl = "https://media.vogue.co.jp/photos/64df24ddd343815066c32ece/master/w_1600%2Cc_limit/VJ-travel-world-bridge-06.jpeg"
)

val mockUser13 = User(
    id = "13",
    username = "Mana",
    fullname = "まな",
    profileImageUrl = "https://cdn.fujiyama-navi.jp/entries/images/000/004/668/original/6688648b-443d-4f6b-a39f-be2240ea06e3.jpg?1516586033"
)

val mockUser14 = User(
    id = "14",
    username = "Taichi",
    fullname = "たいち",
    profileImageUrl = "https://animeanime.jp/imgs/ogp_f/303592.jpg"
)

val mockUser15 = User(
    id = "15",
    username = "Rin",
    fullname = "りん",
    profileImageUrl = "https://www.nta.co.jp/media/tripa/static_contents/nta-tripa/item_images/images/000/066/242/medium/8c11da1d-97de-4c99-96dd-78089cc52f72.jpg?1550676731"
)

val mockTopic1 = Topic(
    id = "1",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1,
        mockTopicImage3,
        mockTopicImage4
    ),
    user = mockUser1,
    votes = 134,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic2 = Topic(
    id = "2",
    title = "終わらぬ冒険: 時空を越えた旅",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage4
    ),
    user = mockUser2,
    votes = 4232,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic3 = Topic(
    id = "3",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1,
        mockTopicImage2,
        mockTopicImage3,
        mockTopicImage4
    ),
    user = mockUser3,
    votes = 987,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic4 = Topic(
    id = "4",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1,
        mockTopicImage4
    ),
    user = mockUser4,
    votes = 756,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic5 = Topic(
    id = "5",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage2,
        mockTopicImage4
    ),
    user = mockUser5,
    votes = 44,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic6 = Topic(
    id = "6",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1,
        mockTopicImage2,
        mockTopicImage3,
        mockTopicImage4
    ),
    user = mockUser6,
    votes = 90,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic7 = Topic(
    id = "7",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage2,
        mockTopicImage3,
        mockTopicImage4
    ),
    user = mockUser7,
    votes = 34,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic8 = Topic(
    id = "8",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage3
    ),
    user = mockUser8,
    votes = 45,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic9 = Topic(
    id = "9",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1,
        mockTopicImage2,
        mockTopicImage4
    ),
    user = mockUser9,
    votes = 201,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic10 = Topic(
    id = "10",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1,
        mockTopicImage4
    ),
    user = mockUser10,
    votes = 1343,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic11 = Topic(
    id = "11",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage2,
        mockTopicImage3
    ),
    user = mockUser11,
    votes = 1342,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic12 = Topic(
    id = "12",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1
    ),
    user = mockUser12,
    votes = 1834,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic13 = Topic(
    id = "13",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1,
        mockTopicImage2,
        mockTopicImage3,
        mockTopicImage4
    ),
    user = mockUser13,
    votes = 3134,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic14 = Topic(
    id = "14",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage3,
        mockTopicImage4
    ),
    user = mockUser14,
    votes = 4134,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)

val mockTopic15 = Topic(
    id = "15",
    title = "猫が可愛い",
    link = "https://www.google.com/",
    description = "猫がいっぱいいるよ",
    images = listOf(
        mockTopicImage1,
        mockTopicImage2
    ),
    user = mockUser15,
    votes = 1354,
    set = null,
    registerTime = LocalDateTime.now(),
    updateTime = LocalDateTime.now()
)