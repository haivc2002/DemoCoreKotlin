package com.example.democorekotlin.utils

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.fDatetime(pattern: String = "dd/MM/yyyy"): String {
    return this.format(DateTimeFormatter.ofPattern(pattern))
}

fun LocalDateTime.weekdayName(): String {
    return when (this.dayOfWeek) {
        DayOfWeek.MONDAY -> "Thứ 2"
        DayOfWeek.TUESDAY -> "Thứ 3"
        DayOfWeek.WEDNESDAY -> "Thứ 4"
        DayOfWeek.THURSDAY -> "Thứ 5"
        DayOfWeek.FRIDAY -> "Thứ 6"
        DayOfWeek.SATURDAY -> "Thứ 7"
        DayOfWeek.SUNDAY -> "Chủ nhật"
    }
}