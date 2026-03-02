package com.example.democorekotlin.feature.atin.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.core.base.BaseViewModel
import com.example.democorekotlin.model.response.Data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AtinMainViewModel @Inject constructor() : BaseViewModel() {

    var pageIndex by mutableIntStateOf(0)
        private set
    var userInfo by mutableStateOf<Data?>(null)
        private set

    fun nameView(): String {
        return when (pageIndex) {
            0 -> "Trang chủ"
            1 -> "Chấm công"
            2 -> "Lịch sử ra vào"
            3 -> "Cá nhân"
            else -> "Trang chủ"
        }
    }

    fun onGetInformation(info: Data?) {
        userInfo = info
    }


    fun onChangeView(index: Int) {
        if (index in 0..3) {
            pageIndex = index
        }
    }

    fun onOpenCheckIn() {
        viewModelScope.launch {
            // TODO: Request camera permission
            // val permissionGranted = requestCameraPermission()
            
            // if (permissionGranted) {
            //     pushNamed(CameraCheckInView.ROUTER)
            //     // Refresh home data after returning
            // } else {
            //     overlay.dialog(
            //         title = "Thông báo",
            //         body = { 
            //             Text("Bạn cần cho phép camera để check-in") 
            //         }
            //     )
            // }
        }
    }
}