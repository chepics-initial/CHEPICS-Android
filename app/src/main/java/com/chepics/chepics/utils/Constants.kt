package com.chepics.chepics.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object Constants {
    const val ONE_TIME_CODE_LENGTH = 4
    const val PASSWORD_LENGTH = 8
    const val TOPIC_TITLE_LENGTH = 100
    const val DESCRIPTION_LENGTH = 300
    const val TOPIC_IMAGE_COUNT = 4
}

fun getDateTimeString(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now()

    if (ChronoUnit.SECONDS.between(dateTime, now) < 60) {
        return "${ChronoUnit.SECONDS.between(dateTime, now)}秒前"
    }

    if (ChronoUnit.MINUTES.between(dateTime, now) < 60) {
        return "${ChronoUnit.MINUTES.between(dateTime, now)}分前"
    }

    if (ChronoUnit.HOURS.between(dateTime, now) < 24) {
        return "${ChronoUnit.HOURS.between(dateTime, now)}時間前"
    }

    if (ChronoUnit.DAYS.between(dateTime, now) < 7) {
        return "${ChronoUnit.DAYS.between(dateTime, now)}日前"
    }

    val formatter = DateTimeFormatter.ofPattern("yyyy年M月d日")
    return dateTime.format(formatter)
}