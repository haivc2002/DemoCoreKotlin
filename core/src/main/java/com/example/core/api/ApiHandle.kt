package com.example.core.api

import okio.IOException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ApiHandle @Inject constructor() {

    suspend fun <T> handleApiCall(
        apiCall: suspend () -> Response<T>
    ): ApiResult<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                response.body()?.let {
                    ApiResult.Success(it)
                } ?: ApiResult.Failure(
                    ApiResult.IS_ERROR,
                    "Empty body"
                )
            } else {
                ApiResult.Failure(
                    response.code(),
                    response.message()
                )
            }
        } catch (e: SocketTimeoutException) {
            ApiResult.Failure(ApiResult.IS_TIME_OUT, e.message)
        } catch (e: UnknownHostException) {
            ApiResult.Failure(ApiResult.IS_NOT_CONNECT, e.message)
        } catch (e: IOException) {
            ApiResult.Failure(ApiResult.IS_DUE_SERVER, e.message)
        } catch (e: Exception) {
            ApiResult.Failure(ApiResult.IS_ERROR, e.message)
        }
    }
}
