package com.shreeramarchakaru.gayatrijapa.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    fun formatDateTime(timestamp: Long?): String {
        return if (timestamp != null && timestamp > 0) {
            dateTimeFormat.format(Date(timestamp))
        } else {
            "Not available"
        }
    }

    fun formatDate(timestamp: Long?): String {
        return if (timestamp != null && timestamp > 0) {
            dateFormat.format(Date(timestamp))
        } else {
            "Not available"
        }
    }

    fun formatTime(timestamp: Long?): String {
        return if (timestamp != null && timestamp > 0) {
            timeFormat.format(Date(timestamp))
        } else {
            "Not available"
        }
    }

    fun getTimeDifference(startTime: Long?, endTime: Long?): String {
        if (startTime == null || endTime == null || startTime >= endTime) {
            return "0 min"
        }

        val diffInMillis = endTime - startTime
        val diffInMinutes = diffInMillis / (1000 * 60)
        val diffInHours = diffInMinutes / 60
        val diffInDays = diffInHours / 24

        return when {
            diffInDays > 0 -> "${diffInDays}d ${diffInHours % 24}h"
            diffInHours > 0 -> "${diffInHours}h ${diffInMinutes % 60}m"
            else -> "${diffInMinutes}m"
        }
    }

    fun isToday(timestamp: Long?): Boolean {
        if (timestamp == null) return false

        val calendar = Calendar.getInstance()
        val today = calendar.get(Calendar.DAY_OF_YEAR)
        val todayYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val timestampDay = calendar.get(Calendar.DAY_OF_YEAR)
        val timestampYear = calendar.get(Calendar.YEAR)

        return today == timestampDay && todayYear == timestampYear
    }

    fun isYesterday(timestamp: Long?): Boolean {
        if (timestamp == null) return false

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = calendar.get(Calendar.DAY_OF_YEAR)
        val yesterdayYear = calendar.get(Calendar.YEAR)

        calendar.timeInMillis = timestamp
        val timestampDay = calendar.get(Calendar.DAY_OF_YEAR)
        val timestampYear = calendar.get(Calendar.YEAR)

        return yesterday == timestampDay && yesterdayYear == timestampYear
    }

    fun getRelativeTimeString(timestamp: Long?): String {
        if (timestamp == null) return "Unknown"

        return when {
            isToday(timestamp) -> "Today, ${formatTime(timestamp)}"
            isYesterday(timestamp) -> "Yesterday, ${formatTime(timestamp)}"
            else -> formatDateTime(timestamp)
        }
    }
}