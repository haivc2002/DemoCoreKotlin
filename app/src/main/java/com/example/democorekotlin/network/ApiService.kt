package com.example.democorekotlin.network

import com.example.core.api.NoAuthenticationRequired
import com.example.democorekotlin.model.request.*
import com.example.democorekotlin.model.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @NoAuthenticationRequired
    @POST("login")
    suspend fun loginAPI(
        @Body request: RequestLogin
    ): Response<ModelLogin>

    @GET("checkInHistory")
    suspend fun checkInHistoryAPI(
        @Query("fromTime", encoded = true) fromTime: String,
        @Query("toTime", encoded = true) toTime: String,
        @Query("status") status: Int = 1,
    ): Response<ModelListCheckIn>

    @GET("userInfo")
    suspend fun userInfoAPI(): Response<ModelInfo>

    @NoAuthenticationRequired
    @GET("https://api.openweathermap.org/data/2.5/weather")
    suspend fun weatherAPI(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = "f564d22bfddbc8aa0d01af2db4eedb1b",
    ) : Response<ModelWeather>

    @NoAuthenticationRequired
    @GET("https://api-bdc.io/data/reverse-geocode-client")
    suspend fun currentLocationAPI(
        @Query("latitude") lat: String,
        @Query("longitude") lon: String,
        @Query("localityLanguage") localityLanguage: String = "vi",
    ) : Response<ModelCurrentLocation>

    @GET("timeKeepingHistory")
    suspend fun timeKeepingHistoryAPI(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
    ) : Response<ModelTimekeeping>

    @POST("changePassword")
    suspend fun changePasswordAPI(
        @Body request: RequestChangePass
    ) : Response<String>

    @POST("checkinFace")
    suspend fun checkinFaceAPI(
        @Body request: RequestCheckInFace
    ) : Response<Boolean>
}
