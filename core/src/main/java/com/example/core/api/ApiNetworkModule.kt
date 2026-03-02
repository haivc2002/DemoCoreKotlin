package com.example.core.api

import com.fasterxml.jackson.databind.DeserializationFeature
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object ApiNetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(ApiLogger())
            .build()

    @Provides
    @Singleton
    fun provideObjectMapper(): ObjectMapper =
        ObjectMapper()
            .registerKotlinModule()
            .configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false
            )

    /**
     * ## Base URL configuration for API requests.
     *
     * The value of @Named("BaseUrl") is defined via `app/build.gradle`
     * using `buildConfigField` in the core project:
     * ```build.gradle.kts
     * android {
     *      defaultConfig {
     *          buildConfigField(
     *              "String",
     *              "BASE_URL",
     *              "\"https://topcam.ai.vn/apis/\""
     *          )
     *      }
     *      buildFeatures {
     *          buildConfig = true
     *      }
     * }
     *```
     * ApiConfig module provides this BaseUrl as a Singleton dependency
     * so it can be injected wherever needed using:
     *
     * Example: create file `ApiConfig` in project main
     * ```kotlin
     * import dagger.Module
     * import dagger.Provides
     * import dagger.hilt.InstallIn
     * import dagger.hilt.components.SingletonComponent
     * import javax.inject.Named
     * import javax.inject.Singleton
     *
     * @Module
     * @InstallIn(SingletonComponent::class)
     * object ApiConfig {
     *     @Provides
     *     @Singleton
     *     @Named("BaseUrl")
     *     fun provideBaseUrl(): String = BuildConfig.BASE_URL
     * }
     * ```
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        mapper: ObjectMapper,
        @Named("BaseUrl") baseUrl: String
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create(mapper))
            .build()

    /**
     * ApiService is created via the generic Retrofit extension function `createService()`.
     *
     * This approach allows Repository classes to remain decoupled from
     * concrete ApiService implementations and avoids repetitive calls to
     * Retrofit.create(ApiService::class.java).
     *
     * The Retrofit instance is injected by Hilt, while the actual ApiService
     * is lazily created using the extension function.
     *
     * This design improves readability, reusability, and keeps service creation
     * consistent across the data layer.
     *
     * Example:
     * ```kotlin
     * import retrofit2.Retrofit
     * import javax.inject.Inject
     * import com.example.core.api.ApiHandle
     * import com.example.core.api.ApiNetworkModule.createService
     * import com.example.core.api.ApiResult
     *
     * class Repository @Inject constructor(
     *     retrofit: Retrofit,
     *     private val apiHandle: ApiHandle
     * ) {
     *     private val apiService: ApiService = retrofit
     *         .createService()
     *
     *     suspend fun loginApi(
     *         phone: String,
     *         password: String
     *     ) : ApiResult<ModelLogin> {
     *         return apiHandle.handleApiCall {
     *             apiService.loginAPI(phone, password)
     *         }
     *     }
     * }
     * ```
     */
    inline fun <reified T> Retrofit.createService(): T =
        create(T::class.java)
}