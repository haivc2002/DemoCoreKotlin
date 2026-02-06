package com.example.democorekotlin.network

import com.example.core.api.ApiHandle
import com.example.core.api.ApiNetworkModule.createService
import com.example.core.api.ApiResult
import com.example.democorekotlin.model.ModelLogin
import retrofit2.Retrofit
import javax.inject.Inject

class Repository @Inject constructor(
    retrofit: Retrofit,
    private val apiHandle: ApiHandle
) {
    private val apiService: ApiService = retrofit
        .createService()

    suspend fun loginApi(
        phone: String,
        password: String
    ) : ApiResult<ModelLogin> {
        return apiHandle.handleApiCall {
            apiService.loginAPI(phone, password)
        }
    }
}
