package com.example.democorekotlin.feature.login

import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.core.api.ApiResult
import com.example.core.base.BaseNavigator.pushReplacementNamed
import com.example.core.base.BaseViewModel
import com.example.core.base.DialogAction
import com.example.core.common.ECWidget
import com.example.core.datastore.DataStorage
import com.example.democorekotlin.feature.atin.main.AtinMainView
import com.example.democorekotlin.model.request.RequestLogin
import com.example.democorekotlin.network.Repository
import com.example.democorekotlin.router.AppRouter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStorage: DataStorage,
) : BaseViewModel() {

    var phone by mutableStateOf("haibt_uat")
    var password by mutableStateOf("Atin@2026")
    var showPassword by mutableStateOf(false)

    fun onLogin() {
        if(phone.isBlank()) {
            overlay.snackBar("Vui lòng nhập số điện thoại")
            return
        }
        if(password.isBlank()) {
            overlay.snackBar("Vui lòng nhập mật khẩu")
            return
        }

        overlay.dialog(
            {
                Text("ahfw")
            },
            title = "Title",
            actions = listOf(
                DialogAction("OK") {
                    overlay.dismiss()
                },
                DialogAction("Cancel") {
                    overlay.dismiss()
                }
            )
        )

//        viewModelScope.launch {
//            overlay.openLoading()
//            val response = repository.loginApi(RequestLogin(phone, password))
//            overlay.dismiss()
//            when (response) {
//                is ApiResult.Success -> {
//                    val token = response
//                        .value.data
//                        ?.accessToken ?: ""
//                    dataStorage.write("TOKEN_KEY",  token)
//                    pushReplacementNamed(AppRouter.ATIN_MAIN_VIEW)
//                }
//                is ApiResult.Failure -> {
//                    overlay.dialog({
//                        ECWidget(response.errorCode)
//                    })
//                }
//            }
//        }
    }
}
