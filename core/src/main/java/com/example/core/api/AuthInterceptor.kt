package com.example.core.api

import com.example.core.datastore.DataStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val dataStorage: DataStorage
) : Interceptor {

    fun getToken(): String? {
        return runBlocking {
            val token = dataStorage.read("TOKEN_KEY") as? String
            token
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val invocation = request.tag(Invocation::class.java)
        val isNoAuth = invocation?.method()?.isAnnotationPresent(NoAuthenticationRequired::class.java) == true
        if (isNoAuth) return chain.proceed(request)
        val token = getToken()
        val newRequest = request.newBuilder().apply {
            token?.let {
                addHeader("Authorization", "Bearer $it")
            }
        }.build()

        return chain.proceed(newRequest)
    }
}

/**
 * Use for api no auth
 * Example:
 * ```kotlin
 * @NoAuthenticationRequired
 * @POST("login")
 * suspend fun loginAPI() ...
 * ```
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class NoAuthenticationRequired


