package com.example.democorekotlin.model.response

data class ModelTimekeeping(
    val timestamp: String?,
    val status: Int?,
    val error: String?,
    val data: List<TimekeepingData>?
)

data class TimekeepingData(
    val personId: String? = null,
    val personCode: String? = null,
    val day: String? = null,
    val firstInTime: String? = null,
    val latestCheckInMinute: Int? = null,
    val eventCheckInUrl: String? = null,
    val deviceCheckIn: String? = null,
    val lastOutTime: String? = null,
    val earliestCheckInMinute: Int? = null,
    val eventCheckOutUrl: String? = null,
    val deviceCheckOut: String? = null,
    val workDays: Double? = null
)