package com.scribesoul.app.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtils {

    fun isSameDay(timestamp: Long, targetDate: LocalDate): Boolean {
        return try {
            val date = Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            date == targetDate
        } catch (e: Exception) {
            false
        }
    }

    fun formatTimeRange(startTimestamp: Long, durationMinutes: Int): String {
        val formatter = DateTimeFormatter.ofPattern("HH.mm")
        val start = Instant.ofEpochMilli(startTimestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
        val end = start.plusMinutes(durationMinutes.toLong())
        return "${start.format(formatter)} - ${end.format(formatter)}"
    }

    fun getFormattedDate(timestamp: Long): String {
        val date = Instant.ofEpochMilli(timestamp)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }
}