package com.example.onwork.ui.helper

import java.text.SimpleDateFormat
import java.util.*

class DateTime {
    companion object {
        private const val defaultTimePattern = "HH:mm"

        fun getDateFormatted(date: Date, pattern: String = defaultTimePattern): String {
            return date.let {
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                simpleDateFormat.format(date)
            }
        }

        fun calculateDifference(startDate: Date, endDate: Date): String {
            var different: Long = endDate.time - startDate.time
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60

            val elapsedHours = different / hoursInMilli
            different %= hoursInMilli
            val elapsedMinutes = different / minutesInMilli
            different %= minutesInMilli
            val elapsedSeconds = different / secondsInMilli

            return String.format(
                "%02d:%02d:%02d",
                elapsedHours, elapsedMinutes, elapsedSeconds
            )
        }
    }
}