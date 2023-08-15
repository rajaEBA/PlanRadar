package com.example.planradar.infrastructure.utils

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd - HH:mm:ss", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

}