package com.example.democorekotlin.network

import com.example.core.api.NoAuthenticationRequired
import com.example.democorekotlin.model.request.*
import com.example.democorekotlin.model.response.*
import retrofit2.http.*

interface ApiService {
    @NoAuthenticationRequired
    @POST("login")
    suspend fun loginAPI(
        @Body request: RequestLogin
    ): ModelLogin

    @GET("checkInHistory")
    suspend fun checkInHistoryAPI(
        @Query("fromTime", encoded = true) fromTime: String,
        @Query("toTime", encoded = true) toTime: String,
        @Query("status") status: Int = 1,
    ): ModelListCheckIn

    @GET("userInfo")
    suspend fun userInfoAPI(): ModelInfo

    @NoAuthenticationRequired
    @GET("https://api.openweathermap.org/data/2.5/weather")
    suspend fun weatherAPI(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String = "f564d22bfddbc8aa0d01af2db4eedb1b",
    ) : ModelWeather

    @NoAuthenticationRequired
    @GET("https://api-bdc.io/data/reverse-geocode-client")
    suspend fun currentLocationAPI(
        @Query("latitude") lat: String,
        @Query("longitude") lon: String,
        @Query("localityLanguage") localityLanguage: String = "vi",
    ) : ModelCurrentLocation

    @GET("timeKeepingHistory")
    suspend fun timeKeepingHistoryAPI(
        @Query("fromDate") fromDate: String,
        @Query("toDate") toDate: String,
    ) : ModelTimekeeping

    @POST("changePassword")
    suspend fun changePasswordAPI(
        @Body request: RequestChangePass
    ) : String

    @POST("checkinFace")
    suspend fun checkinFaceAPI(
        @Body request: RequestCheckInFace
    ) : Boolean
}
