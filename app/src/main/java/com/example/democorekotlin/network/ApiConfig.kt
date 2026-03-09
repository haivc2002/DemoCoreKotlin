package com.example.democorekotlin.network

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiConfig {

    @Provides
    @Named("BaseUrl")
    fun provideBaseUrl(): String = "https://smfapi.atin.vn/gw/api/v1/mobile/"

    @Provides
    @Named("AuthInterceptor")
    fun provideAuthInterceptor(): Interceptor? = null

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
