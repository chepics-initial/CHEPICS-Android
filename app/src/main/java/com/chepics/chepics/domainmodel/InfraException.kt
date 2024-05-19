package com.chepics.chepics.domainmodel

sealed class InfraException : Exception() {
    data class Server(
        override val message: String,
        val statusCode: String,
        val errorCode: String
    ) : InfraException()
}