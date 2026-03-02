package com.example.democorekotlin.model.request

data class RequestChangePass(
    val exPass: String,
    val newPassword: String,
    val newPasswordConfirmation: String,
)