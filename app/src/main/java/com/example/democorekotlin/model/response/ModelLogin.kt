package com.example.democorekotlin.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ModelLogin(
    val timestamp: String?,
    val status: Int?,
    val error: String?,
    val data: LoginData?
)

data class LoginData(
    @JsonProperty("access_token")
    val accessToken: String?,
    val expires: String?,
    @JsonProperty("refresh_token")
    val refreshToken: String?,
    val fullName: String?,
    val username: String?,
    val userId: Int?,
    val comId: Int?,
    val error: String?
)
