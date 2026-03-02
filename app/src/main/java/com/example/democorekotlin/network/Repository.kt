package com.example.democorekotlin.network

import com.example.core.api.ApiHandle
import com.example.core.api.ApiNetworkModule.createService
import com.example.core.api.ApiResult
import com.example.democorekotlin.model.request.*
import com.example.democorekotlin.model.response.*
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    retrofit: Retrofit,
    private val apiHandle: ApiHandle,
) {
    private val apiService: ApiService = retrofit
        .createService()

    suspend fun loginApi(
        request: RequestLogin
    ) : ApiResult<ModelLogin> {
        return apiHandle.handleApiCall {
            apiService.loginAPI(request)
        }
    }

    suspend fun checkInHistoryApi(
        fromTime: String,
        toTime: String,
    ) : ApiResult<ModelListCheckIn> {
        return apiHandle.handleApiCall {
            apiService.checkInHistoryAPI(fromTime, toTime)
        }
    }

    suspend fun userInfoApi() : ApiResult<ModelInfo> {
        return apiHandle.handleApiCall {
            apiService.userInfoAPI()
        }
    }

    suspend fun weatherApi(
        lat: String,
        lon: String
    ) : ApiResult<ModelWeather> {
        return apiHandle.handleApiCall {
            apiService.weatherAPI(lat, lon)
        }
    }

    suspend fun currentLocationApi(
        lat: String,
        lon: String
    ) : ApiResult<ModelCurrentLocation> {
        return apiHandle.handleApiCall {
            apiService.currentLocationAPI(lat, lon)
        }
    }

    suspend fun timeKeepingHistoryApi(
        fromDate: String,
        toDate: String
    ) : ApiResult<ModelTimekeeping> {
        return apiHandle.handleApiCall {
            apiService.timeKeepingHistoryAPI(fromDate, toDate)
        }
    }

    suspend fun changePasswordApi(
        request: RequestChangePass
    ) : ApiResult<String> {
        return apiHandle.handleApiCall {
            apiService.changePasswordAPI(request)
        }
    }

    suspend fun checkinFaceApi(
        request: RequestCheckInFace
    ) : ApiResult<Boolean> {
        return apiHandle.handleApiCall {
            apiService.checkinFaceAPI(request)
        }
    }
}
