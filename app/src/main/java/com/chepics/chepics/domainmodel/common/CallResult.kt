package com.chepics.chepics.domainmodel.common

sealed class CallResult<out T: Any> {
    data class Success<out T: Any>(val data: T): CallResult<T>()

    data class Error(
        val exception: Exception
    ): CallResult<Nothing>()

    fun <R: Any> mapSuccess(transform: (T) -> R): CallResult<R> = when (val result = this) {
        is Success -> Success(transform(result.data))
        is Error -> Error(exception = result.exception)
    }

    fun mapError(transform: (Exception) -> Exception): CallResult<T> {
        return when (val result = this) {
            is Success -> {
                result
            }
            is Error -> {
                Error(transform(result.exception))
            }
        }
    }
}

fun <T: Any> CallResult<T>.dataOrThrow() = when (this) {
    is CallResult.Success<T> -> data
    is CallResult.Error -> throw exception
}