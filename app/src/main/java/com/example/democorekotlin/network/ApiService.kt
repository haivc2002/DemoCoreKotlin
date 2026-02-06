package com.example.democorekotlin.network

import com.example.democorekotlin.model.ModelLogin
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("loginParentApi")
    suspend fun loginAPI(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Response<ModelLogin>

}
