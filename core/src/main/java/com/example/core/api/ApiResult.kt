package com.example.core.api

/**
 * Represents the result of an API call.
 *
 * This sealed class is used to model success and failure states
 * returned from remote or local data sources.
 *
 * It is typically mapped in the data layer and consumed by
 * ViewModel to convert into UI state.
 *
 * @param T The type of data returned when the request succeeds.
 */
sealed class ApiResult<out T> {

    /**
     * Represents a successful API response.
     *
     * @param value The data returned from the API.
     */
    data class Success<T>(
        val value: T
    ) : ApiResult<T>()

    /**
     * Represents a failed API response.
     *
     * @param errorCode A predefined error code describing the failure type.
     * @param message Optional error message, usually from server or exception.
     */
    data class Failure(
        val errorCode: Int,
        val message: String? = null
    ) : ApiResult<Nothing>()

    companion object {

        /**
         * Generic success indicator.
         * Can be used when API returns a custom "status" field instead of HTTP code.
         */
        const val IS_OK = 1

        /**
         * Unknown or unhandled error.
         * Used as a fallback when no specific error mapping is possible.
         */
        const val IS_ERROR = -555

        /**
         * HTTP-related error (non-2xx responses).
         * Example: 400, 403, 404, 500...
         */
        const val IS_HTTP = -444

        /**
         * No internet connection available.
         * Typically thrown when device is offline.
         */
        const val IS_NOT_CONNECT = -111

        /**
         * Request timeout.
         * Occurs when the server does not respond within the expected time.
         */
        const val IS_TIME_OUT = -222

        /**
         * Server-side error.
         * Indicates that the server is reachable but failed to process the request.
         */
        const val IS_DUE_SERVER = -333

        /**
         * Authentication token has expired or is invalid.
         * Commonly mapped from HTTP 401.
         */
        const val TOKEN_EXPIRATION = 401
    }
}
