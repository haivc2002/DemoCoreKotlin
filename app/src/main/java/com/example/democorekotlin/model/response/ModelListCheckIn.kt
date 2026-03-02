package com.example.democorekotlin.model.response

data class ModelListCheckIn(
    val timestamp: String? = null,
    val status: Int? = null,
    val error: String? = null,
    val data: List<CheckInData>? = null
)

data class CheckInData(
    val eventId: String? = null,
    val personId: String? = null,
    val fullName: String? = null,
    val personCode: String? = null,
    val locationName: String? = null,
    val areaName: String? = null,
    val deviceName: String? = null,
    val deviceType: Int? = null,
    val accessTime: String? = null,
    val status: Int? = null,
    val locationMap: String? = null,
    val eventImageUrl: String? = null,
    val notification: String? = null
)