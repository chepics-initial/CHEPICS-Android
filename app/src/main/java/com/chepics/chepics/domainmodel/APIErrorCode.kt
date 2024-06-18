package com.chepics.chepics.domainmodel

object APIErrorCode {
    const val USED_EMAIL = "USED_EMAIL"
    const val CODE_INCORRECT_OR_EXPIRED = "CODE_INCORRECT_OR_EXPIRED"
    const val NOT_CONFIRMED_EMAIL = "NOT_CONFIRMED_EMAIL"
    const val EMAIL_OR_PASSWORD_INCORRECT = "EMAIL_OR_PASSWORD_INCORRECT"
    const val RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND"
    const val INTERNAL_SERVER_ERROR = "INTERNAL_SERVER_ERROR"
    const val INVALID_ACCESS_TOKEN = "INVALID_ACCESS_TOKEN"
    const val INVALID_REFRESH_TOKEN = "INVALID_REFRESH_TOKEN"
    const val ERROR_SET_NOT_PICKED = "ERROR_SET_NOT_PICKED"
    const val ERROR_TOPIC_NOT_PICKED = "ERROR_TOPIC_NOT_PICKED"
}