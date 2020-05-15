package com.example.onwork.ui.helper

import java.text.SimpleDateFormat
import java.util.*

class DateTime {
    companion object {
        private const val defaultTimePattern = "HH:mm"

        fun getDateFormatted(
            date: Date,
            pattern: String = defaultTimePattern,
            beautify: Boolean = false
        ): String {
            var output = date.let {
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.US)
                simpleDateFormat.format(date)
            }

            if (beautify) {
                val fmt = SimpleDateFormat(pattern, Locale.US)

                if (output == fmt.format(Date())) {
                    output = "Today"
                } else {
                    val cal = Calendar.getInstance()
                    cal.time = Date()
                    cal[Calendar.DAY_OF_MONTH] = cal.get(Calendar.DAY_OF_MONTH) - 1

                    if (output == fmt.format(cal.time)) {
                        output = "Yesterday"
                    }
                }
            }

            return output
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