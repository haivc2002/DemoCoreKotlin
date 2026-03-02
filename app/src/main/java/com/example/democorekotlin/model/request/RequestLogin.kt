package com.example.democorekotlin.model.request

data class RequestLogin(
    val username: String,
    val password: String,
    val fcmToken: String = "",
    val deviceId: String = "",
    val refreshToken: String? = null,
)
