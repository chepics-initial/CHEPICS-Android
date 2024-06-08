package com.chepics.chepics.infra.datasource.api

import com.chepics.chepics.domainmodel.InfraException
import com.chepics.chepics.domainmodel.common.CallResult
import com.chepics.chepics.domainmodel.contentOrNull
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import okhttp3.ResponseBody
import retrofit2.Response
import timber.log.Timber

inline fun <reified T : Any> safeApiCall(call: () -> Response<T>): CallResult<T> =
    safeApiCallInternal(call) { it.body() }

@Suppress("TooGenericExceptionCaught", "UNCHECKED_CAST")
inline fun <reified T : Any, U : Any> safeApiCallInternal(
    call: () -> Response<T>,
    responseToBody: (Response<T>) -> U?
): CallResult<U> = try {
    val r = call()
    val body = responseToBody(r)
    if (r.isSuccessful) {
        if (body != null) {
            CallResult.Success(body)
        } else if (Unit is T) {
            CallResult.Success(Unit as U)
        } else if (r.code() == 204) {
            CallResult.Error(Exception(EMPTY_RESPONSE))
        } else {
            CallResult.Error(parseErrorBody(r.errorBody()))
        }
    } else {
        CallResult.Error(parseErrorBody(r.errorBody()))
    }
} catch (e: IllegalArgumentException) {
    Timber.e(e)
    CallResult.Error(e)
} catch (e: Exception) {
    CallResult.Error(e)
}

fun parseErrorBody(errorBody: ResponseBody?): Exception {
    val errorJson =
        errorBody?.string() ?: return IllegalArgumentException("response error body is empty")
    val errorJsonElement = Json.parseToJsonElement(errorJson)

    val statusCode = errorJsonElement.jsonObject[JSON_KEY_STATUS_CODE].contentOrNull
        ?: return IllegalArgumentException("contents error status code is empty")
    val message = errorJsonElement.jsonObject[JSON_KEY_MESSAGE].contentOrNull
        ?: return IllegalArgumentException("contents error message is empty")
    val errorCode = errorJsonElement.jsonObject[JSON_KEY_ERROR_CODE].contentOrNull
        ?: return IllegalArgumentException("contents error code is empty")
    return InfraException.Server(message = message, statusCode = statusCode, errorCode = errorCode)
}

private const val JSON_KEY_STATUS_CODE = "status_code"
private const val JSON_KEY_MESSAGE = "message"
private const val JSON_KEY_ERROR_CODE = "error_code"
const val EMPTY_RESPONSE = "EMPTY_RESPONSE"