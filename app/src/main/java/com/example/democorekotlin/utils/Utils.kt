package com.example.democorekotlin.utils

import androidx.compose.ui.graphics.Color
import com.example.democorekotlin.res.MyColor
import java.time.LocalDateTime

class Utils {

    companion object {
        fun daysInMonth(dateTime: LocalDateTime): Int {
            return dateTime.toLocalDate().lengthOfMonth()
        }

        fun itemStatus(
            isLeaveWork: Boolean,      // Nghỉ làm
            isWeekend: Boolean,        // Cuối tuần
            isFutureDate: Boolean,     // Ngày tương lai
            isRealTkp: Boolean,        // Đúng giờ
            isLateTkp: Boolean,        // Muộn
            isEarlyLeaveTkp: Boolean,  // Về sớm
        ): ItemStatus {
            if (isLeaveWork && !isFutureDate)
                return ItemStatus("Nghỉ làm", MyColor.red)
            if (isWeekend)
                return ItemStatus("Ngày cuối tuần", MyColor.sliver)
            if (isFutureDate)
                return ItemStatus("", MyColor.sliver)
            if (isRealTkp)
                return ItemStatus("Đúng giờ", MyColor.lavenderBlue)
            if (isLateTkp)
                return ItemStatus("Muộn giờ", MyColor.orange)
            if (isEarlyLeaveTkp)
                return ItemStatus("Muộn giờ", MyColor.lavenderPink)
            return ItemStatus("Nghỉ làm", MyColor.red)
        }
    }

    data class ItemStatus(
        val name: String,
        val color: Color
    )
}