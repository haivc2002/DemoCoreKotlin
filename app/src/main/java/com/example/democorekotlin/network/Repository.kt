package com.example.democorekotlin.network

import com.example.core.api.ApiResult
import com.example.core.base.BaseRepository
import com.example.democorekotlin.model.request.*
import com.example.democorekotlin.model.response.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val apiService: ApiService
) : BaseRepository() {

    suspend fun loginApi(
        request: RequestLogin
    ) : ApiResult<ModelLogin> {
        return execute {
            apiService.loginAPI(request)
        }
    }

    suspend fun checkInHistoryApi(
        fromTime: String,
        toTime: String,
    ) : ApiResult<ModelListCheckIn> {
        return execute {
            apiService.checkInHistoryAPI(fromTime, toTime)
        }
    }

    suspend fun userInfoApi() : ApiResult<ModelInfo> {
        return execute {
            apiService.userInfoAPI()
        }
    }

    suspend fun weatherApi(
        lat: String,
        lon: String
    ) : ApiResult<ModelWeather> {
        return execute {
            apiService.weatherAPI(lat, lon)
        }
    }

    suspend fun currentLocationApi(
        lat: String,
        lon: String
    ) : ApiResult<ModelCurrentLocation> {
        return execute {
            apiService.currentLocationAPI(lat, lon)
        }
    }

    suspend fun timeKeepingHistoryApi(
        fromDate: String,
        toDate: String
    ) : ApiResult<ModelTimekeeping> {
        return execute {
            apiService.timeKeepingHistoryAPI(fromDate, toDate)
        }
    }

    suspend fun changePasswordApi(
        request: RequestChangePass
    ) : ApiResult<String> {
        return execute {
            apiService.changePasswordAPI(request)
        }
    }

    suspend fun checkinFaceApi(
        request: RequestCheckInFace
    ) : ApiResult<Boolean> {
        return execute {
            apiService.checkinFaceAPI(request)
        }
    }
}
