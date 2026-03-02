package com.example.democorekotlin.model.response

data class ModelInfo(
    val timestamp: String? = null,
    val status: Int? = null,
    val error: String? = null,
    val data: Data? = null
)

data class Data(
    val userId: Int? = null,
    val userName: String? = null,
    val email: String? = null,
    val emailConfirmed: Boolean? = null,
    val phoneNumber: String? = null,
    val phoneNumberConfirmed: Boolean? = null,
    val twoFactorEnabled: Boolean? = null,
    val compId: Int? = null,
    val deptId: Int? = null,
    val personId: String? = null,
    val personCode: String? = null,
    val fullname: String? = null,
    val position: String? = null,
    val jobDuties: String? = null,
    val birthday: Any? = null,
    val gender: Int? = null,
    val education: Any? = null,
    val ethenic: Any? = null,
    val religion: Any? = null,
    val nation: Any? = null,
    val marital: Any? = null,
    val homeAddress: Any? = null,
    val avatarPath: Any? = null,
    val passport: Any? = null,
    val identityCard: String? = null
)
