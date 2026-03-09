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
}