package com.example.core.base

import com.example.core.api.ApiResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseRepository {

    protected suspend fun <T> execute(
        apiCall: suspend () -> T
    ): ApiResult<T> {
        return withContext(Dispatchers.IO) {
            try {
                ApiResult.Success(apiCall.invoke())
            } catch (e: SocketTimeoutException) {
                ApiResult.Failure(
                    ApiResult.IS_TIME_OUT,
                    "Kết nối đã hết thời gian chờ. Vui lòng thử lại.",
                    e
                )
            } catch (e: UnknownHostException) {
                ApiResult.Failure(
                    ApiResult.IS_NOT_CONNECT,
                    "Không thể kết nối đến server. Vui lòng kiểm tra kết nối mạng.",
                    e
                )
            } catch (e: HttpException) {
                ApiResult.Failure(
                    e.code(),
                    "Đã xảy ra lỗi HTTP",
                    e
                )
            } catch (e: IOException) {
                ApiResult.Failure(
                    ApiResult.IS_DUE_SERVER,
                    "Lỗi kết nối mạng. Vui lòng thử lại.",
                    e
                )
            } catch (e: Exception) {
                ApiResult.Failure(
                    ApiResult.IS_ERROR,
                    "Đã xảy ra lỗi không xác định",
                    e
                )

            }
        }
    }
}
